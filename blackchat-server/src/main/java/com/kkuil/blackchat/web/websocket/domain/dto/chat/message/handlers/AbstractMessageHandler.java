package com.kkuil.blackchat.web.websocket.domain.dto.chat.message.handlers;

import com.kkuil.blackchat.domain.entity.Message;
import com.kkuil.blackchat.web.chat.domain.enums.MessageTypeEnum;
import com.kkuil.blackchat.web.websocket.domain.dto.chat.AbstractChatMessageBaseReq;
import com.kkuil.blackchat.web.websocket.domain.dto.chat.message.handlers.factory.MessageHandlerFactory;
import com.kkuil.blackchat.web.websocket.domain.vo.request.ChatMessageReq;
import com.kkuil.blackchat.web.websocket.domain.vo.response.ChatMessageResp;
import jakarta.annotation.PostConstruct;

/**
 * @Author Kkuil
 * @Date 2023/9/28 10:01
 * @Description 消息处理器
 */
public abstract class AbstractMessageHandler {

    /**
     * 初始化注册策略
     */
    @PostConstruct
    private void init() {
        MessageHandlerFactory.register(getMessageTypeEnum().getType(), this);
    }

    /**
     * 消息类型
     *
     * @return 消息类型
     */
    abstract MessageTypeEnum getMessageTypeEnum();

    /**
     * 校验消息——保存前校验
     *
     * @param chatMessageReq 请求消息体
     * @param uid            发送消息的用户ID
     */
    public abstract void checkMessage(ChatMessageReq<? extends AbstractChatMessageBaseReq> chatMessageReq, Long uid);

    /**
     * 保存消息
     *
     * @param message        消息
     * @param chatMessageReq 请求消息体
     */
    public abstract void saveMessage(Message message, ChatMessageReq<? extends AbstractChatMessageBaseReq> chatMessageReq);

    /**
     * 构建响应消息体
     *
     * @param message 消息对象
     * @return 响应消息体
     */
    public abstract ChatMessageResp buildChatMessageResp(Message message);

    /**
     * 被回复时——展示的消息
     *
     * @param message 消息体
     * @return 被回复时——展示的消息
     */
    public abstract Object showReplyMessage(Message message);

    /**
     * 会话列表——展示的消息
     *
     * @param message 消息体
     * @return 会话列表——展示的消息
     */
    public abstract String showContactMessage(Message message);
}
