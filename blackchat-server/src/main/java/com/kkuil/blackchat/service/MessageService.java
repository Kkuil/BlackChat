package com.kkuil.blackchat.service;

import com.kkuil.blackchat.domain.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kkuil.blackchat.web.websocket.domain.dto.chat.AbstractChatMessageBaseReq;
import com.kkuil.blackchat.web.websocket.domain.vo.request.ChatMessageReq;
import com.kkuil.blackchat.web.websocket.domain.vo.response.ChatMessageResp;

/**
 * @Author Kkuil
 * @Date 2023/9/28 15:50
 * @Description 针对表【message(消息表)】的数据库操作Service
 */
public interface MessageService {

    /**
     * 检查用户发送的消息
     *
     * @param uid            用户ID
     * @param chatMessageReq 消息体
     */
    void check(Long uid, ChatMessageReq<? extends AbstractChatMessageBaseReq> chatMessageReq);

    /**
     * 二次保存消息（保存一些不同消息类型该保存的消息，比如说文本消息保存艾特用户，图片消息保存链接等）
     *
     * @param message        消息体
     * @param chatMessageReq 消息体
     */
    void save(Message message, ChatMessageReq<? extends AbstractChatMessageBaseReq> chatMessageReq);

    /**
     * 构建消息返回体
     *
     * @param message 消息对象
     * @return 消息返回体
     */
    ChatMessageResp buildChatMessageResp(Message message, ChatMessageReq<? extends AbstractChatMessageBaseReq> chatMessageReq);

    /**
     * 检查回复消息
     *
     * @param chatMessageReq 消息体
     */
    void checkReplyMessage(ChatMessageReq<? extends AbstractChatMessageBaseReq> chatMessageReq);

}