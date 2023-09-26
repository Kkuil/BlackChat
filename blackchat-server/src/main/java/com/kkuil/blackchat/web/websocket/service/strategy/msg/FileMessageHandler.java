package com.kkuil.blackchat.web.websocket.service.strategy.msg;

import com.kkuil.blackchat.web.websocket.domain.entity.Message;
import com.kkuil.blackchat.web.websocket.domain.enums.msg.MessageTypeEnum;
import com.kkuil.blackchat.web.websocket.domain.vo.request.ChatMessageReq;
import org.springframework.stereotype.Component;


/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description 图片消息
 */
@Component
public class FileMessageHandler extends AbstractMessageHandler {
    @Override
    MessageTypeEnum getMessageTypeEnum() {
        return MessageTypeEnum.FILE;
    }

    @Override
    public void checkMessage(ChatMessageReq request, Long uid) {
        // TODO 检查消息
        // ......
    }

    @Override
    public void saveMessage(Message msg, ChatMessageReq request) {
        // TODO 保存消息
        // ......
    }

    @Override
    public Object showMessage(Message msg) {
        return msg.getExtra().getFileMsg();
    }

    @Override
    public Object showReplyMessage(Message msg) {
        return null;
    }

    @Override
    public String showContactMessage(Message msg) {
        return null;
    }
}
