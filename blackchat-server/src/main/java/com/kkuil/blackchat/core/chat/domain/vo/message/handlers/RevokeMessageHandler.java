package com.kkuil.blackchat.core.chat.domain.vo.message.handlers;

import com.kkuil.blackchat.core.chat.domain.enums.MessageTypeEnum;
import com.kkuil.blackchat.core.chat.domain.vo.response.message.ChatMessageResp;
import com.kkuil.blackchat.core.chat.domain.vo.response.message.body.TextMessageRespBody;
import com.kkuil.blackchat.core.websocket.domain.vo.request.ChatMessageReq;
import com.kkuil.blackchat.domain.entity.Message;
import org.springframework.stereotype.Service;

/**
 * @Author Kkuil
 * @Date 2023/9/28 10:18
 * @Description 文本消息处理器
 */
@Service
public class RevokeMessageHandler extends AbstractMessageHandler<TextMessageRespBody> {

    /**
     * 消息类型
     *
     * @return 消息类型
     */
    @Override
    MessageTypeEnum getMessageTypeEnum() {
        return MessageTypeEnum.REVOKE;
    }

    /**
     * 校验消息——保存前校验
     *
     * @param chatMessageReq 请求消息体
     * @param uid            发送消息的用户ID
     */
    @Override
    public void checkMessage(ChatMessageReq chatMessageReq, Long uid) {

    }

    /**
     * 保存消息
     *
     * @param message        消息
     * @param chatMessageReq 请求消息体
     */
    @Override
    public void saveMessage(Message message, ChatMessageReq chatMessageReq) {
    }

    /**
     * 构建响应消息体
     *
     * @param message 消息对象
     * @param builder 构造器
     * @return 响应消息体
     */
    @Override
    public ChatMessageResp.Message buildChatMessageResp(Message message, ChatMessageResp.Message.MessageBuilder builder) {
        TextMessageRespBody textMessageRespBody = this.buildResponseBody(message);
        return builder.body(textMessageRespBody).build();
    }

    /**
     * 构建消息返回体对象
     *
     * @param message 消息体对象
     * @return 消息体对象
     */
    @Override
    public TextMessageRespBody buildResponseBody(Message message) {
        TextMessageRespBody.TextMessageRespBodyBuilder builder = TextMessageRespBody.builder().content(message.getContent());
        return builder.build();
    }

    /**
     * 被回复时——展示的消息
     *
     * @param message 消息体
     * @return 被回复时——展示的消息
     */
    @Override
    public String showInReplyMessage(Message message) {
        return message.getContent();
    }

    /**
     * 会话列表——展示的消息
     *
     * @param message 消息体
     * @return 会话列表——展示的消息
     */
    @Override
    public String showInContactMessage(Message message) {
        return message.getContent();
    }
}
