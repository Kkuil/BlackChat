package com.kkuil.blackchat.web.websocket.service.strategy.msg;

import com.kkuil.blackchat.web.websocket.domain.entity.Message;
import com.kkuil.blackchat.web.websocket.domain.enums.msg.MessageTypeEnum;
import com.kkuil.blackchat.web.websocket.domain.vo.request.ChatMessageReq;
import jakarta.annotation.PostConstruct;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description 消息处理器抽象类
 */
public abstract class AbstractMessageHandler {

    @PostConstruct
    private void init() {
        MessageHandlerFactory.register(getMessageTypeEnum().getType(), this);
    }

    /**
     * 消息类型
     *
     * @return MessageTypeEnum
     */
    abstract MessageTypeEnum getMessageTypeEnum();

    /**
     * 校验消息——保存前校验
     *
     * @param req 请求的消息体
     * @param uid 用户ID
     */
    public abstract void checkMessage(ChatMessageReq req, Long uid);

    /**
     * 保存消息
     *
     * @param message 消息对象
     * @param req     消息请求对象
     */
    public abstract void saveMessage(Message message, ChatMessageReq req);

    /**
     * 展示消息
     *
     * @param message 消息对象
     * @return 消息
     */
    public abstract Object showMessage(Message message);

    /**
     * 被回复时——展示的消息
     *
     * @param message 消息对象
     * @return 消息
     */
    public abstract Object showReplyMessage(Message message);

    /**
     * 会话列表——展示的消息
     *
     * @param message 消息对象
     * @return 消息
     */
    public abstract String showContactMessage(Message message);

}
