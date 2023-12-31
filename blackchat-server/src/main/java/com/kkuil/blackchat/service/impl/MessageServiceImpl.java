package com.kkuil.blackchat.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.kkuil.blackchat.cache.UserCache;
import com.kkuil.blackchat.core.chat.domain.enums.MessageTypeEnum;
import com.kkuil.blackchat.core.chat.domain.vo.request.message.RevokeMessageReq;
import com.kkuil.blackchat.dao.MessageDAO;
import com.kkuil.blackchat.dao.UserDAO;
import com.kkuil.blackchat.domain.dto.IpDetail;
import com.kkuil.blackchat.domain.dto.IpInfo;
import com.kkuil.blackchat.domain.dto.UserBaseInfo;
import com.kkuil.blackchat.domain.entity.Message;
import com.kkuil.blackchat.domain.enums.error.ChatErrorEnum;
import com.kkuil.blackchat.event.MessageSendEvent;
import com.kkuil.blackchat.service.MessageService;
import com.kkuil.blackchat.utils.AssertUtil;
import com.kkuil.blackchat.core.chat.domain.vo.message.handlers.AbstractMessageHandler;
import com.kkuil.blackchat.core.chat.domain.vo.message.handlers.factory.MessageHandlerFactory;
import com.kkuil.blackchat.core.websocket.domain.vo.request.ChatMessageReq;
import com.kkuil.blackchat.core.chat.domain.vo.response.message.ChatMessageResp;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

/**
 * @Author Kkuil
 * @Date 2023/9/28 15:51
 * @Description 针对表【message(消息表)】的数据库操作Service实现
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageDAO messageDao;

    @Resource
    private UserDAO userDao;

    @Resource
    private UserCache userCache;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;


    /**
     * 检查用户发送的消息
     *
     * @param chatMessageReq 消息体
     */
    @Override
    public void check(Long uid, ChatMessageReq chatMessageReq) {
        // 1. 检查回复消息
        this.checkReplyMessage(chatMessageReq);
        // 2. 根据消息类型获取相应的处理器，对不同消息进行处理
        Integer messageType = chatMessageReq.getMessageType();
        AbstractMessageHandler<Object> handler = MessageHandlerFactory.getStrategyNoNull(messageType);
        // 3. 对不同的消息进行校验（例如：1. 文本：敏感词；2. 图片：带颜色的图片（这个需要用到AI技术，我觉得能实现 哈哈哈）,还有是否有艾特全体成员的权限 等等）
        handler.checkMessage(chatMessageReq, uid);
    }

    /**
     * 二次保存消息（保存一些不同消息类型该保存的消息，比如说文本消息保存艾特用户，图片消息保存链接等）
     *
     * @param message        消息体
     * @param chatMessageReq 消息体
     */
    @Override
    public void save(Message message, ChatMessageReq chatMessageReq) {
        // 1. 根据消息类型获取相应的处理器，对不同消息进行处理
        Integer messageType = chatMessageReq.getMessageType();
        AbstractMessageHandler<Object> handler = MessageHandlerFactory.getStrategyNoNull(messageType);
        // 2. 对不同的消息进行消息入库
        handler.saveMessage(message, chatMessageReq);
    }

    /**
     * 构建消息返回体
     *
     * @param messageId 消息ID
     * @return 消息返回体
     */
    @Override
    public ChatMessageResp buildChatMessageResp(Long messageId, Boolean isCreatTime, Boolean isRoomId) {
        // 0. 构建回复消息
        ChatMessageResp.ReplyMsg replyMsg = this.buildReplyMsg(messageId);
        // 1. 根据消息类型获取相应的处理器，对不同消息进行处理
        Message message = messageDao.getById(messageId);
        AbstractMessageHandler<Object> handler = MessageHandlerFactory.getStrategyNoNull(message.getType());
        // 2. 返回构建消息
        ChatMessageResp.Message.MessageBuilder builder = ChatMessageResp.Message.builder()
                .id(message.getId())
                .sendTime(isCreatTime ? message.getCreateTime() : message.getUpdateTime())
                .type(message.getType())
                .reply(replyMsg);
        if (isRoomId) {
            builder.roomId(message.getRoomId());
        }
        // 2.1 消息对象
        ChatMessageResp.Message msg = handler.buildChatMessageResp(message, builder);
        // 2.2 用户对象
        UserBaseInfo baseUserInfo = userCache.getBaseUserInfoByUid(message.getFromUid());

        IpInfo ipInfo = Optional.ofNullable(baseUserInfo.getIpInfo()).orElse(new IpInfo());
        IpDetail ipDetail = Optional.ofNullable(ipInfo.getUpdateIpDetail()).orElse(new IpDetail());
        ChatMessageResp.UserInfo userInfo = ChatMessageResp.UserInfo.builder()
                .uid(message.getFromUid())
                .place(ipDetail.getCity())
                .build();
        return ChatMessageResp.builder()
                .message(msg)
                .fromUser(userInfo).build();
    }

    /**
     * 检查回复消息
     *
     * @param chatMessageReq 请求消息体
     */
    @Override
    public void checkReplyMessage(ChatMessageReq chatMessageReq) {
        Long roomId = chatMessageReq.getRoomId();
        Long replyMsgId = chatMessageReq.getReplyMessageId();
        if (replyMsgId != null) {
            // 2.1 判断回复的消息是否存在
            Message message = messageDao.getById(replyMsgId);
            AssertUtil.isNotEmpty(message, ChatErrorEnum.MESSAGE_NOT_EXIST.getMsg());
            // 2.2 判断回复的消息和发送的消息是否在同一个房间内
            Long replyMsgRoomId = message.getRoomId();
            AssertUtil.equal(roomId, replyMsgRoomId, ChatErrorEnum.REPLY_MESSAGE_NOT_MATCH.getMsg());
        }
    }

    /**
     * 构建回复消息
     *
     * @param messageId 消息ID
     * @return 消息
     */
    @Override
    public ChatMessageResp.ReplyMsg buildReplyMsg(Long messageId) {
        Message message = messageDao.getById(messageId);
        Long replyMessageId = message.getReplyMessageId();
        ChatMessageResp.ReplyMsg replyMsg;
        if (ObjectUtil.isNull(replyMessageId)) {
            replyMsg = null;
        } else {
            Message replyMessage = messageDao.getById(replyMessageId);
            String name = userDao.getById(replyMessage.getFromUid()).getName();

            Object body;

            // 判断回复消息是否是撤回消息
            if(MessageTypeEnum.REVOKE.getType().equals(replyMessage.getType())) {
                body = "原消息已撤回";
            } else {
                body = MessageHandlerFactory.getStrategyNoNull(replyMessage.getType()).showInReplyMessage(replyMessage);
            }

            replyMsg = ChatMessageResp.ReplyMsg
                    .builder()
                    .id(replyMessage.getId())
                    .uid(replyMessage.getFromUid())
                    .name(name)
                    .type(replyMessage.getType())
                    .body(body)
                    .canCallback(0)
                    .gapCount(replyMessage.getGapCount())
                    .build();
        }
        return replyMsg;
    }

    /**
     * 撤回消息
     *
     * @param uid     用户ID
     * @param request 请求信息
     * @return 消息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean revoke(Long uid, RevokeMessageReq request) {
        Long id = request.getMsgId();

        // 1. 判断用户是否有权利撤回该消息
        UserBaseInfo baseInfo = userCache.getBaseUserInfoByUid(uid);
        Boolean isOwner = messageDao.hasPower(baseInfo, id);
        AssertUtil.isTrue(isOwner, ChatErrorEnum.NOT_ALLOWED_REVOKE.getMsg());

        // 2. 判断发消息时间是否大于三分钟
        Message msg = messageDao.getById(request.getMsgId());
        AssertUtil.isNotEmpty(msg, ChatErrorEnum.MESSAGE_NOT_EXIST.getMsg());
        Date sendTime = msg.getCreateTime();
        AssertUtil.isFalse(DateUtil.between(sendTime, new Date(), DateUnit.MINUTE) > 3, ChatErrorEnum.NOT_ALLOWED_TO_REVOKE_MESSAGE_OVER_THREE_MINUTES.getMsg());

        // 3. 撤回消息
        Message message = messageDao.revoke(baseInfo, id);

        // 4. 推送消息
        applicationEventPublisher.publishEvent(new MessageSendEvent(this, message));
        return true;
    }
}
