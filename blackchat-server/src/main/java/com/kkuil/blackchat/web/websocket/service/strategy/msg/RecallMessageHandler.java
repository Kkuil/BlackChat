package com.kkuil.blackchat.web.websocket.service.strategy.msg;

import com.kkuil.blackchat.web.websocket.domain.entity.Message;
import com.kkuil.blackchat.web.websocket.domain.enums.msg.MessageTypeEnum;
import com.kkuil.blackchat.web.websocket.domain.vo.request.ChatMessageReq;
import org.springframework.stereotype.Component;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description 撤回文本消息
 */
@Component
public class RecallMessageHandler extends AbstractMessageHandler {

    /**
     * 消息类型
     *
     * @return MessageTypeEnum
     */
    @Override
    MessageTypeEnum getMessageTypeEnum() {
        return MessageTypeEnum.RECALL;
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

    /**
     * 展示消息
     *
     * @param message 消息对象
     * @return 消息
     */
    @Override
    public Object showMessage(Message message) {
        return null;
    }

    /**
     * 被回复时——展示的消息
     *
     * @param message 消息对象
     * @return 消息
     */
    @Override
    public Object showReplyMessage(Message message) {
        return null;
    }

    /**
     * 会话列表——展示的消息
     *
     * @param message 消息对象
     * @return 消息
     */
    @Override
    public String showContactMessage(Message message) {
        return null;
    }
}
