package com.kkuil.blackchat.web.websocket.service.strategy.msg;

import com.kkuil.blackchat.web.websocket.domain.entity.Message;
import com.kkuil.blackchat.web.websocket.domain.enums.msg.MessageTypeEnum;
import com.kkuil.blackchat.web.websocket.domain.vo.request.ChatMessageReq;
import org.springframework.stereotype.Component;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description 系统消息 
 */
@Component
public class SystemMessageHandler extends AbstractMessageHandler {

    @Override
    MessageTypeEnum getMessageTypeEnum() {
        return MessageTypeEnum.SYSTEM;
    }

    /**
     * 校验消息——保存前校验
     *
     * @param req 请求的消息体
     * @param uid 用户ID
     */
    @Override
    public void checkMessage(ChatMessageReq req, Long uid) {

    }

    /**
     * 保存消息
     *
     * @param message 消息对象
     * @param req     消息请求对象
     */
    @Override
    public void saveMessage(Message message, ChatMessageReq req) {

    }

    @Override
    public Object showMessage(Message msg) {
        return msg.getContent();
    }

    @Override
    public Object showReplyMessage(Message msg) {
        return msg.getContent();
    }

    @Override
    public String showContactMessage(Message msg) {
        return msg.getContent();
    }
}