package com.kkuil.blackchat.web.chat.service.impl;

import com.kkuil.blackchat.cache.GroupCache;
import com.kkuil.blackchat.cache.UserCache;
import com.kkuil.blackchat.dao.*;
import com.kkuil.blackchat.domain.dto.UserBaseInfo;
import com.kkuil.blackchat.domain.entity.Message;
import com.kkuil.blackchat.domain.entity.Room;
import com.kkuil.blackchat.domain.entity.User;
import com.kkuil.blackchat.domain.enums.error.ChatErrorEnum;
import com.kkuil.blackchat.domain.vo.response.CursorPageBaseResp;
import com.kkuil.blackchat.service.MessageService;
import com.kkuil.blackchat.service.RoomService;
import com.kkuil.blackchat.utils.AssertUtil;
import com.kkuil.blackchat.utils.ResultUtil;
import com.kkuil.blackchat.web.chat.domain.enums.RoomTypeEnum;
import com.kkuil.blackchat.web.chat.domain.vo.request.ChatMemberCursorReq;
import com.kkuil.blackchat.web.chat.domain.vo.request.ChatMemberExtraResp;
import com.kkuil.blackchat.web.chat.domain.vo.response.ChatMemberResp;
import com.kkuil.blackchat.web.websocket.domain.enums.ChatActiveStatusEnum;
import com.kkuil.blackchat.web.websocket.domain.vo.request.ChatMessageReq;
import com.kkuil.blackchat.web.chat.domain.vo.response.ChatMessageResp;
import com.kkuil.blackchat.web.chat.service.ChatService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kkuil.blackchat.web.chat.service.adapter.GroupMemberAdapter;

import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/9/28 9:52
 * @Description 聊天业务类
 */
@Service
public class ChatServiceImpl implements ChatService {

    @Resource
    private UserCache userCache;

    @Resource
    private RoomService roomService;

    @Resource
    private MessageService messageService;

    @Resource
    private MessageDAO messageDao;

    @Resource
    private RoomDAO roomDao;

    @Resource
    private GroupCache groupCache;

    @Resource
    private UserDAO userDao;

    /**
     * 发送消息
     *
     * @param chatMessageReq 消息体
     * @return 响应体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultUtil<ChatMessageResp> send(Long uid, ChatMessageReq chatMessageReq) {
        // 1. 检查用户与房间的关系
        roomService.check(uid, chatMessageReq);

        // 2. 检查用户与其发的消息的情况
        messageService.check(uid, chatMessageReq);

        // 3. 保存消息(除了与回复消息间隔数)
        Message message = messageDao.saveByUidAndChatMessageReq(uid, chatMessageReq);

        // 4. 保存与回复消息间隔数
        Long roomId = chatMessageReq.getRoomId();
        Long messageId = message.getId();
        Long replyMessageId = chatMessageReq.getReplyMessageId();
        messageDao.saveGapCount(roomId, messageId, replyMessageId);

        // 5. 让各自的消息处理器保存消息
        messageService.save(message, chatMessageReq);

        // 6. 构建消息响应体
        ChatMessageResp chatMessageResp = messageService.buildChatMessageResp(messageId, chatMessageReq);
        return ResultUtil.success(chatMessageResp);
    }

    /**
     * 获取成员信息
     *
     * @param uid                 用户ID
     * @param chatMemberCursorReq 成员请求信息
     * @return 成员信息
     */
    @Override
    public CursorPageBaseResp<ChatMemberResp> listMember(Long uid, ChatMemberCursorReq chatMemberCursorReq) {
        Long roomId = chatMemberCursorReq.getRoomId();
        // 0. 检查房间号是否存在
        Room room = roomDao.getById(roomId);
        AssertUtil.isNotEmpty(room, ChatErrorEnum.ROOM_NOT_EXIST.getMsg());

        // 1. 检查用户是否在房间内(用户不在房间内，不让获取群列表)
        Boolean isMember = roomService.checkRoomMembership(roomId, uid);
        AssertUtil.isTrue(isMember, ChatErrorEnum.NOT_IN_GROUP.getMsg());

        Integer roomType = room.getType();
        // 2. 判断当前房间的类型是否是群聊
        boolean isGroup = RoomTypeEnum.GROUP.getType().equals(roomType);
        AssertUtil.isTrue(isGroup, ChatErrorEnum.NOT_GROUP.getMsg());

        // 3. 获取数据
        CursorPageBaseResp<User> userCursorPageBaseResp;
        Integer activeStatus = chatMemberCursorReq.getActiveStatus();
        Integer pageSize = chatMemberCursorReq.getPageSize();
        // 判断热点群聊
        List<Long> uidList;
        if (room.getHotFlag() == 1) {
            uidList = null;
        } else {
            uidList = groupCache.getGroupUidByRoomId(roomId);
        }
        ChatMemberExtraResp chatMemberExtraResp = new ChatMemberExtraResp();
        if (ChatActiveStatusEnum.ONLINE.getStatus().equals(activeStatus)) {
            // 3.1 获取在线成员
            userCursorPageBaseResp = userDao.getCursorPage(uidList, chatMemberCursorReq);
            // 3.1.1 判断当前获取到的数量是否小于pageSize
            int size = userCursorPageBaseResp.getList().size();
            if (size < pageSize) {
                // 3.1.1.1 小于则从离线列表进行填补
                int remainCount = chatMemberCursorReq.getPageSize() - size;
                chatMemberCursorReq.setPageSize(remainCount);
                chatMemberCursorReq.setActiveStatus(ChatActiveStatusEnum.OFFLINE.getStatus());
                CursorPageBaseResp<User> cursorPage = userDao.getCursorPage(uidList, chatMemberCursorReq);
                userCursorPageBaseResp.getList().addAll(cursorPage.getList());
                userCursorPageBaseResp.setCursor(cursorPage.getCursor());
                chatMemberExtraResp.setActiveStatus(ChatActiveStatusEnum.OFFLINE.getStatus());
                // 3.1.1.2 只要走了这里面的逻辑，就告诉前端这不是最后一页数据
                userCursorPageBaseResp.setIsLast(false);
            } else {
                chatMemberExtraResp.setActiveStatus(ChatActiveStatusEnum.ONLINE.getStatus());
            }
        } else {
            // 3.2 获取离线成员
            userCursorPageBaseResp = userDao.getCursorPage(uidList, chatMemberCursorReq);
            chatMemberCursorReq.setActiveStatus(ChatActiveStatusEnum.OFFLINE.getStatus());
        }
        if (uidList != null) {
            chatMemberExtraResp.setTotalCount(uidList.size());
        } else {
            Long totalCount = userCache.getTotalCount();
            chatMemberExtraResp.setTotalCount(Math.toIntExact(totalCount));
        }
        userCursorPageBaseResp.setExtraInfo(chatMemberExtraResp);

        // 4. 组装数据
        List<ChatMemberResp> list = userCursorPageBaseResp.getList().stream().map(user -> {
            UserBaseInfo baseUserInfo = userCache.getBaseUserInfo(user.getId());
            return ChatMemberResp.builder()
                    .uid(user.getId())
                    .name(baseUserInfo.getName())
                    .avatar(baseUserInfo.getAvatar())
                    .activeStatus(baseUserInfo.getActiveStatus())
                    .build();
        }).toList();
        return GroupMemberAdapter.buildChatMemberCursorPage(list, userCursorPageBaseResp);
    }
}
