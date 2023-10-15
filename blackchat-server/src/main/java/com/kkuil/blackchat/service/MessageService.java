package com.kkuil.blackchat.service;

import com.kkuil.blackchat.core.chat.domain.vo.request.message.RevokeMessageReq;
import com.kkuil.blackchat.domain.entity.Message;
import com.kkuil.blackchat.core.websocket.domain.vo.request.ChatMessageReq;
import com.kkuil.blackchat.core.chat.domain.vo.response.message.ChatMessageResp;

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
    void check(Long uid, ChatMessageReq chatMessageReq);

    /**
     * 二次保存消息（保存一些不同消息类型该保存的消息，比如说文本消息保存艾特用户，图片消息保存链接等）
     *
     * @param message        消息体
     * @param chatMessageReq 消息体
     */
    void save(Message message, ChatMessageReq chatMessageReq);

    /**
     * 构建消息返回体
     *
     * @param messageId    消息ID
     * @param isCreateTime 是否使用创建时间作为消息时间
     * @param isRoomId     是否需要房间ID
     * @return 消息返回体
     */
    ChatMessageResp buildChatMessageResp(Long messageId, Boolean isCreateTime, Boolean isRoomId);

    /**
     * 检查回复消息
     *
     * @param chatMessageReq 消息体
     */
    void checkReplyMessage(ChatMessageReq chatMessageReq);

    /**
     * 构建回复消息
     *
     * @param messageId 消息ID
     * @return 回复消息对象
     */
    ChatMessageResp.ReplyMsg buildReplyMsg(Long messageId);

    /**
     * 撤回消息
     *
     * @param uid     用户ID
     * @param request 请求信息
     * @return 消息
     */
    Boolean revoke(Long uid, RevokeMessageReq request);
}
