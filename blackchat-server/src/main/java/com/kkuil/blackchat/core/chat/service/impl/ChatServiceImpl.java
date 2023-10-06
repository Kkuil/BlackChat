package com.kkuil.blackchat.core.chat.service.impl;

import com.kkuil.blackchat.cache.GroupCache;
import com.kkuil.blackchat.cache.UserCache;
import com.kkuil.blackchat.dao.*;
import com.kkuil.blackchat.domain.dto.UserBaseInfo;
import com.kkuil.blackchat.domain.entity.Message;
import com.kkuil.blackchat.domain.entity.Room;
import com.kkuil.blackchat.domain.entity.User;
import com.kkuil.blackchat.domain.enums.error.ChatErrorEnum;
import com.kkuil.blackchat.domain.vo.response.CursorPageBaseResp;
import com.kkuil.blackchat.event.MessageSendEvent;
import com.kkuil.blackchat.service.MessageService;
import com.kkuil.blackchat.service.RoomService;
import com.kkuil.blackchat.utils.AssertUtil;
import com.kkuil.blackchat.utils.ResultUtil;
import com.kkuil.blackchat.core.chat.domain.enums.RoomTypeEnum;
import com.kkuil.blackchat.core.chat.domain.vo.message.handlers.AbstractMessageHandler;
import com.kkuil.blackchat.core.chat.domain.vo.message.handlers.factory.MessageHandlerFactory;
import com.kkuil.blackchat.core.chat.domain.vo.request.member.ChatMemberCursorReq;
import com.kkuil.blackchat.core.chat.domain.vo.request.member.ChatMemberExtraResp;
import com.kkuil.blackchat.core.chat.domain.vo.request.message.ChatMessageCursorReq;
import com.kkuil.blackchat.core.chat.domain.vo.response.member.ChatMemberResp;
import com.kkuil.blackchat.core.websocket.domain.enums.ChatActiveStatusEnum;
import com.kkuil.blackchat.core.websocket.domain.vo.request.ChatMessageReq;
import com.kkuil.blackchat.core.chat.domain.vo.response.message.ChatMessageResp;
import com.kkuil.blackchat.core.chat.service.ChatService;
import com.kkuil.blackchat.core.websocket.service.adapter.MessageAdapter;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kkuil.blackchat.core.chat.service.adapter.GroupMemberAdapter;

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

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

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
        messageDao.saveGapCount(roomId, replyMessageId, messageId);

        // 5. 让各自的消息处理器保存消息
        messageService.save(message, chatMessageReq);

        // 6. 构建消息响应体
        ChatMessageResp chatMessageResp = messageService.buildChatMessageResp(messageId);

        // 7. 发送消息事件
        applicationEventPublisher.publishEvent(new MessageSendEvent(this, messageId));
        return ResultUtil.success(chatMessageResp);
    }

    /**
     * 获取成员列表
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

        // 判断热点群聊
        List<Long> uidList;
        if (room.isHotRoom()) {
            uidList = null;
        } else {
            uidList = groupCache.getGroupUidByRoomId(roomId);
            // 1. 检查用户是否在房间内(用户不在房间内，不让获取群列表)
            Boolean isMember = roomService.checkRoomMembership(roomId, uid);
            AssertUtil.isTrue(isMember, ChatErrorEnum.NOT_IN_GROUP.getMsg());
        }

        // 3. 获取数据
        CursorPageBaseResp<User> userCursorPageBaseResp;
        Integer activeStatus = chatMemberCursorReq.getActiveStatus();
        Integer pageSize = chatMemberCursorReq.getPageSize();

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
            UserBaseInfo baseUserInfo = userCache.getBaseUserInfoByUid(user.getId());
            return ChatMemberResp.builder()
                    .uid(user.getId())
                    .name(baseUserInfo.getName())
                    .avatar(baseUserInfo.getAvatar())
                    .activeStatus(baseUserInfo.getActiveStatus())
                    .build();
        }).toList();
        return GroupMemberAdapter.buildChatMemberCursorPage(list, userCursorPageBaseResp);
    }

    /**
     * 获取消息列表
     *
     * @param chatMessageCursorReq 消息请求参数
     * @param uid                  用户ID
     * @return 响应参数
     */
    @Override
    public CursorPageBaseResp<ChatMessageResp> listMessage(Long uid, ChatMessageCursorReq chatMessageCursorReq) {
        Long roomId = chatMessageCursorReq.getRoomId();
        // 0. 检查房间号是否存在
        Room room = roomDao.getById(roomId);
        AssertUtil.isNotEmpty(room, ChatErrorEnum.ROOM_NOT_EXIST.getMsg());

        // 判断是否是大群聊
        if (!room.isHotRoom()) {
            // 1. 检查用户是否在房间内(用户不在房间内，不让获取群消息)
            Boolean isMember = roomService.checkRoomMembership(roomId, uid);
            AssertUtil.isTrue(isMember, ChatErrorEnum.NOT_IN_GROUP.getMsg());
        }

        // 2. 获取群消息
        CursorPageBaseResp<Message> messageCursorPageBaseResp = messageDao.getCursorPage(chatMessageCursorReq);

        // 3. 构建消息
        List<ChatMessageResp> list = messageCursorPageBaseResp.getList().stream()
                .map(message -> {
                    Long fromUid = message.getFromUid();
                    UserBaseInfo baseUserInfo = userCache.getBaseUserInfoByUid(fromUid);

                    // 3.1 构建用户信息
                    ChatMessageResp.UserInfo fromUser = ChatMessageResp.UserInfo.builder()
                            .uid(fromUid)
                            .name(baseUserInfo.getName())
                            .avatar(baseUserInfo.getAvatar())
                            .ipInfo(baseUserInfo.getIpInfo())
                            .build();

                    // 3.2 构建消息信息
                    // 3.2.1 根据消息类型生产相应的处理器
                    AbstractMessageHandler<Object> handler = MessageHandlerFactory.getStrategyNoNull(message.getType());

                    // 3.2.2 构建消息体
                    Object body = handler.buildResponseBody(message);

                    // 3.2.3 构建回复消息体
                    ChatMessageResp.ReplyMsg replyMsg = messageService.buildReplyMsg(message.getId());
                    ChatMessageResp.Message msg = ChatMessageResp.Message.builder()
                            .id(message.getId())
                            .sendTime(message.getCreateTime())
                            .type(message.getType())
                            .body(body)
                            .reply(replyMsg)
                            .build();

                    return ChatMessageResp.builder()
                            .fromUser(fromUser)
                            .message(msg)
                            .build();
                }).toList();

        return MessageAdapter.buildChatMessageRespList(messageCursorPageBaseResp, list);
    }
}
