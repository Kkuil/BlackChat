package com.kkuil.blackchat.web.websocket.service;

import com.kkuil.blackchat.utils.ResultUtil;
import com.kkuil.blackchat.web.websocket.domain.dto.chat.AbstractChatMessageBaseReq;
import com.kkuil.blackchat.web.websocket.domain.vo.request.ChatMessageReq;
import com.kkuil.blackchat.web.websocket.domain.vo.response.ChatMessageResp;

/**
 * @Author Kkuil
 * @Date 2023/9/28 9:52
 * @Description 聊天接口
 */
public interface ChatService {

    /**
     * 发送消息
     *
     * @param chatMessageReq 消息体
     * @param uid            用户ID
     * @return 响应体
     */
    ResultUtil<ChatMessageResp> send(Long uid, ChatMessageReq<? extends AbstractChatMessageBaseReq> chatMessageReq);

}
