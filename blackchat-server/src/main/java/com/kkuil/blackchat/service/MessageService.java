package com.kkuil.blackchat.service;

import com.kkuil.blackchat.domain.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kkuil.blackchat.web.websocket.domain.dto.chat.AbstractChatMessageBaseReq;
import com.kkuil.blackchat.web.websocket.domain.vo.request.ChatMessageReq;

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
}
