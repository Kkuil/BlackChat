package com.kkuil.blackchat.web.websocket.domain.dto.chat.message.handlers;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.kkuil.blackchat.dao.*;
import com.kkuil.blackchat.domain.entity.Message;
import com.kkuil.blackchat.domain.entity.Room;
import com.kkuil.blackchat.domain.enums.error.ChatErrorEnum;
import com.kkuil.blackchat.service.RoleService;
import com.kkuil.blackchat.service.RoomService;
import com.kkuil.blackchat.service.impl.ChatGroupSpecialMemberEnum;
import com.kkuil.blackchat.utils.AssertUtil;
import com.kkuil.blackchat.utils.sensitive.SensitiveWordBs;
import com.kkuil.blackchat.web.chat.domain.enums.GroupRoleEnum;
import com.kkuil.blackchat.web.chat.domain.enums.MessageTypeEnum;
import com.kkuil.blackchat.web.chat.domain.enums.RoomTypeEnum;
import com.kkuil.blackchat.web.chat.domain.vo.request.TextMessageReqVO;
import com.kkuil.blackchat.web.websocket.domain.dto.chat.AbstractChatMessageBaseReq;
import com.kkuil.blackchat.web.websocket.domain.vo.request.ChatMessageReq;
import jakarta.annotation.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Kkuil
 * @Date 2023/9/28 10:18
 * @Description 文本消息处理器
 */
@Resource
public class TextMessageHandler extends AbstractMessageHandler {

    @Resource
    private SensitiveWordBs sensitiveWordBs;

    @Resource
    private MessageDAO messageDao;

    @Resource
    private UserDAO userDao;

    @Resource
    private RoomService roomService;

    @Resource
    private GroupMemberDAO groupMemberDao;

    @Resource
    private RoomDAO roomDao;

    /**
     * 消息类型
     *
     * @return 消息类型
     */
    @Override
    MessageTypeEnum getMessageTypeEnum() {
        return MessageTypeEnum.TEXT;
    }

    /**
     * 校验消息——保存前校验
     *
     * @param chatMessageReq 请求消息体
     * @param uid            发送消息的用户ID
     */
    @Override
    public void checkMessage(ChatMessageReq<? extends AbstractChatMessageBaseReq> chatMessageReq, Long uid) {
        // 1. 将消息体转换为文本消息体
        AbstractChatMessageBaseReq body = chatMessageReq.getBody();
        Long roomId = chatMessageReq.getRoomId();
        TextMessageReqVO textMessage = BeanUtil.toBean(body, TextMessageReqVO.class);
        // 2. 检查回复消息
        Long replyMsgId = textMessage.getReplyMsgId();
        if (replyMsgId != null) {
            // 2.1 判断回复的消息是否存在
            Message message = messageDao.getById(replyMsgId);
            AssertUtil.isNotEmpty(message, ChatErrorEnum.MESSAGE_NOT_EXIST.getMsg());
            // 2.2 判断回复的消息和发送的消息是否在同一个房间内
            Long replyMsgRoomId = message.getRoomId();
            AssertUtil.equal(roomId, replyMsgRoomId, ChatErrorEnum.REPLY_MESSAGE_NOT_MATCH.getMsg());
        }
        // 3. 检查艾特消息
        List<Long> atUidList = textMessage.getAtUidList();
        boolean isAtUidListNotEmpty = CollectionUtil.isNotEmpty(atUidList);
        if (isAtUidListNotEmpty) {
            // 3.1 判断是否有重复艾特
            HashSet<Long> atUidSet = new HashSet<>(atUidList);
            AssertUtil.equal(atUidList.size(), atUidSet.size(), ChatErrorEnum.AT_USER_REPEAT.getMsg());
            // 3.2 判断艾特用户是否存在
            List<Long> notExistList = atUidList.stream().filter(id -> userDao.getById(id) == null).toList();
            AssertUtil.isFalse(notExistList.size() > 0, ChatErrorEnum.AT_USER_NOT_EXIST.getMsg());
            // 3.3 判断艾特的人是否在同一房间内（也就是说判断当前房间内是否存在被艾特的人）
            Long[] atUidArr = ArrayUtil.toArray(atUidList, Long.class);
            Boolean isSameRoom = roomService.checkRoomMembership(roomId, atUidArr);
            AssertUtil.isTrue(isSameRoom, ChatErrorEnum.NOT_IN_GROUP.getMsg());
            // 3.4 判断艾特用户中是否有全体用户
            boolean isContainAll = atUidList.contains(ChatGroupSpecialMemberEnum.ALL.getId());
            Room room = roomDao.getById(roomId);
            Integer roomType = room.getType();
            if (isContainAll && roomType.equals(RoomTypeEnum.GROUP.getType())) {
                // 3.4.1 有，则判断是否有权限
                List<Integer> authorities = new ArrayList<>();
                authorities.add(GroupRoleEnum.LEADER.getType());
                authorities.add(GroupRoleEnum.MANAGER.getType());
                Boolean isAuth = groupMemberDao.hasAuthority(roomId, uid, authorities);
                AssertUtil.isTrue(isAuth, ChatErrorEnum.NO_AUTH.getMsg());
            }
            // 3.4.2 没有，则直接跳过
        }
    }

    /**
     * 保存消息
     *
     * @param message        消息
     * @param chatMessageReq 请求消息体
     */
    @Override
    public void saveMessage(Message message, ChatMessageReq<? extends AbstractChatMessageBaseReq> chatMessageReq) {

    }

    /**
     * 展示消息
     *
     * @param message 消息
     * @return 前端展示消息的文本
     */
    @Override
    public Object showMessage(Message message) {
        return null;
    }

    /**
     * 被回复时——展示的消息
     *
     * @param message 消息体
     * @return 被回复时——展示的消息
     */
    @Override
    public Object showReplyMessage(Message message) {
        return null;
    }

    /**
     * 会话列表——展示的消息
     *
     * @param message 消息体
     * @return 会话列表——展示的消息
     */
    @Override
    public String showContactMessage(Message message) {
        return null;
    }
}
