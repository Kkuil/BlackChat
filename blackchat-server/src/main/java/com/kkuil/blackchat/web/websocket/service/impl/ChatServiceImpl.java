package com.kkuil.blackchat.web.websocket.service.impl;

import com.kkuil.blackchat.service.MessageService;
import com.kkuil.blackchat.service.RoomService;
import com.kkuil.blackchat.utils.ResultUtil;
import com.kkuil.blackchat.web.websocket.domain.dto.chat.AbstractChatMessageBaseReq;
import com.kkuil.blackchat.web.websocket.domain.vo.request.ChatMessageReq;
import com.kkuil.blackchat.web.websocket.domain.vo.response.ChatMessageResp;
import com.kkuil.blackchat.web.websocket.service.ChatService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author Kkuil
 * @Date 2023/9/28 9:52
 * @Description 聊天业务类
 */
@Service
public class ChatServiceImpl implements ChatService {

    @Resource
    private RoomService roomService;

    @Resource
    private MessageService messageService;

    /**
     * 发送消息
     *
     * @param chatMessageReq 消息体
     * @return 响应体
     */
    @Override
    public ResultUtil<ChatMessageResp> send(Long uid, ChatMessageReq<? extends AbstractChatMessageBaseReq> chatMessageReq) {
        // 1. 检查用户与房间的关系
        roomService.check(uid, chatMessageReq);
        // 2. 检查用户与其发的消息的情况
        messageService.check(uid, chatMessageReq);
    }
}
