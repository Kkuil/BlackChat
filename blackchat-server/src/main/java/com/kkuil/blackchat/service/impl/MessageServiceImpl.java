package com.kkuil.blackchat.service.impl;

import com.kkuil.blackchat.service.MessageService;
import com.kkuil.blackchat.web.websocket.domain.dto.chat.AbstractChatMessageBaseReq;
import com.kkuil.blackchat.web.websocket.domain.dto.chat.message.handlers.AbstractMessageHandler;
import com.kkuil.blackchat.web.websocket.domain.dto.chat.message.handlers.factory.MessageHandlerFactory;
import com.kkuil.blackchat.web.websocket.domain.vo.request.ChatMessageReq;
import org.springframework.stereotype.Service;

/**
 * @Author Kkuil
 * @Date 2023/9/28 15:51
 * @Description 针对表【message(消息表)】的数据库操作Service实现
 */
@Service
public class MessageServiceImpl implements MessageService {

    /**
     * 检查用户发送的消息
     *
     * @param chatMessageReq 消息体
     */
    @Override
    public void check(Long uid, ChatMessageReq<? extends AbstractChatMessageBaseReq> chatMessageReq) {
        // 1. 根据消息类型获取相应的处理器，对不同消息进行处理
        Integer messageType = chatMessageReq.getMessageType();
        AbstractMessageHandler handler = MessageHandlerFactory.getStrategyNoNull(messageType);
        // 1.1 对不同的消息进行校验（例如：1. 文本：敏感词；2. 图片：带颜色的图片（这个需要用到AI技术，我觉得能实现 哈哈哈）还有是否有艾特全体成员的权限 等等）
        handler.checkMessage(chatMessageReq, uid);
    }
}
