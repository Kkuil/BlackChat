package com.kkuil.blackchat.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.kkuil.blackchat.cache.*;
import com.kkuil.blackchat.constant.UserConst;
import com.kkuil.blackchat.core.chat.domain.enums.RoomTypeEnum;
import com.kkuil.blackchat.core.chat.domain.vo.message.handlers.factory.MessageHandlerFactory;
import com.kkuil.blackchat.core.contact.domain.adapter.ContactAdapter;
import com.kkuil.blackchat.core.contact.domain.vo.request.ChatContactCursorReq;
import com.kkuil.blackchat.core.contact.domain.vo.request.ChatReadMessageReq;
import com.kkuil.blackchat.core.contact.domain.vo.response.ChatContactCursorResp;
import com.kkuil.blackchat.core.contact.domain.vo.response.FriendResp;
import com.kkuil.blackchat.dao.ContactDAO;
import com.kkuil.blackchat.dao.MessageDAO;
import com.kkuil.blackchat.dao.RoomDAO;
import com.kkuil.blackchat.dao.RoomFriendDAO;
import com.kkuil.blackchat.domain.bo.contact.ContactWithActiveMsg;
import com.kkuil.blackchat.domain.bo.room.FriendBaseInfo;
import com.kkuil.blackchat.domain.bo.room.GroupBaseInfo;
import com.kkuil.blackchat.domain.bo.room.RoomBaseInfo;
import com.kkuil.blackchat.domain.dto.UserBaseInfo;
import com.kkuil.blackchat.domain.entity.Contact;
import com.kkuil.blackchat.domain.entity.Message;
import com.kkuil.blackchat.domain.enums.error.ChatErrorEnum;
import com.kkuil.blackchat.domain.enums.error.CommonErrorEnum;
import com.kkuil.blackchat.domain.vo.response.CursorPageBaseResp;
import com.kkuil.blackchat.service.ContactService;
import com.kkuil.blackchat.utils.AssertUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author Kkuil
 * @Date 2023/10/5 17:14
 * @Description 针对表【contact(会话列表)】的数据库操作Service实现
 */
@Service
public class ContactServiceImpl implements ContactService {

    @Resource
    private RoomCache roomCache;

    @Resource
    private MessageDAO messageDao;

    @Resource
    private UserCache userCache;

    @Resource
    private RoomGroupCache roomGroupCache;

    @Resource
    private RoomFriendCache roomFriendCache;

    @Resource
    private ContactDAO contactDao;

    /**
     * 获取会话列表
     *
     * @param uid     用户ID
     * @param request 请求
     * @return 会话列表
     */
    @Override
    public CursorPageBaseResp<ChatContactCursorResp, Date> listContact(Long uid, ChatContactCursorReq request) {
        CursorPageBaseResp<ContactWithActiveMsg, Date> cursorPage = contactDao.getCursorPage(uid, request);

        List<ChatContactCursorResp> list = cursorPage.getList().stream().map(contact -> {
            ChatContactCursorResp chatContactCursorResp = new ChatContactCursorResp();
            Long roomId = contact.getRoomId();

            chatContactCursorResp.setRoomId(roomId);
            if (!UserConst.TEMP_USER_UID.equals(uid)) {
                chatContactCursorResp.setActiveTime(Optional.ofNullable(contact.getActiveTime()).orElse(new Date()));
            }

            // 通过房间ID获取房间信息
            RoomBaseInfo roomBaseInfo = roomCache.getRoomBaseInfoById(roomId);
            chatContactCursorResp.setType(roomBaseInfo.getType());
            chatContactCursorResp.setHotFlag(roomBaseInfo.getHotFlag());

            Long lastMsgId = contact.getLastMsgId();
            if (lastMsgId != null) {
                Message message = messageDao.getById(lastMsgId);
                AssertUtil.isNotEmpty(message, ChatErrorEnum.MESSAGE_NOT_EXIST.getMsg());

                Long fromUid = message.getFromUid();
                UserBaseInfo baseInfo = userCache.getBaseUserInfoByUid(fromUid);
                String showInContactMessage = MessageHandlerFactory.getStrategyNoNull(message.getType()).showInContactMessage(message);
                chatContactCursorResp.setText(baseInfo.getName() + ": " + showInContactMessage);
            } else {
                // 1. 判断有没有最新消息
                chatContactCursorResp.setText("暂无消息");
            }

            if (RoomTypeEnum.GROUP.getType().equals(roomBaseInfo.getType())) {
                // 群聊
                GroupBaseInfo groupBaseInfo = roomGroupCache.getBaseInfoById(roomId);
                AssertUtil.isNotEmpty(groupBaseInfo, CommonErrorEnum.SYSTEM_ERROR.getMsg());

                // 设房间名
                String name = groupBaseInfo.getName();
                if (StrUtil.isEmpty(name)) {
                    chatContactCursorResp.setName("没有名字的房间");
                } else {
                    chatContactCursorResp.setName(name);
                }

                // 房间头像
                chatContactCursorResp.setAvatar(groupBaseInfo.getAvatar());

            } else {
                // 单聊
                FriendBaseInfo roomFriendBaseInfo = roomFriendCache.getBaseInfoById(roomId);
                AssertUtil.isNotEmpty(roomFriendBaseInfo, CommonErrorEnum.SYSTEM_ERROR.getMsg());

                Long uid1 = roomFriendBaseInfo.getUid1();
                Long uid2 = roomFriendBaseInfo.getUid2();
                if (uid.equals(uid1)) {
                    UserBaseInfo userBaseInfo = userCache.getBaseUserInfoByUid(uid2);
                    chatContactCursorResp.setName(userBaseInfo.getName());
                    chatContactCursorResp.setAvatar(userBaseInfo.getAvatar());
                } else if (uid.equals(uid2)) {
                    UserBaseInfo userBaseInfo = userCache.getBaseUserInfoByUid(uid1);
                    chatContactCursorResp.setName(userBaseInfo.getName());
                    chatContactCursorResp.setAvatar(userBaseInfo.getAvatar());
                }
            }

            Integer count = messageDao.getUnReadCountByReadTime(roomId, contact.getReadTime());
            chatContactCursorResp.setUnreadCount(count);

            return chatContactCursorResp;
        }).toList();

        return ContactAdapter.buildContactCursorPage(list, cursorPage);
    }

    /**
     * 用户阅读信息上报
     *
     * @param uid     用户ID
     * @param request 上报信息
     * @return 是否已上报
     */
    @Override
    public boolean readMessage(Long uid, ChatReadMessageReq request) {
        Long roomId = request.getRoomId();
        // 临时用户不进行上报
        if (UserConst.TEMP_USER_UID.equals(uid)) {
            return true;
        } else {
            Contact contact = contactDao.getByRoomId(uid, roomId);
            if (!ObjectUtil.isNull(contact)) {
                Contact update = new Contact();
                update.setId(contact.getId());
                update.setReadTime(new Date());
                contactDao.updateById(update);
            } else {
                Contact insert = new Contact();
                insert.setUid(uid);
                insert.setRoomId(roomId);
                insert.setReadTime(new Date());
                contactDao.save(insert);
            }
        }
        return true;
    }
}
