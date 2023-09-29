package com.kkuil.blackchat.web.websocket.service.impl;

import com.kkuil.blackchat.dao.MessageDAO;
import com.kkuil.blackchat.domain.entity.Message;
import com.kkuil.blackchat.service.MessageService;
import com.kkuil.blackchat.service.RoomService;
import com.kkuil.blackchat.utils.ResultUtil;
import com.kkuil.blackchat.web.websocket.domain.dto.chat.AbstractChatMessageBaseReq;
import com.kkuil.blackchat.web.websocket.domain.vo.request.ChatMessageReq;
import com.kkuil.blackchat.web.websocket.domain.vo.response.ChatMessageResp;
import com.kkuil.blackchat.web.websocket.service.ChatService;
import com.kkuil.blackchat.web.websocket.service.adapter.MessageAdapter;
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

    @Resource
    private MessageDAO messageDao;

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

        // 3. 保存消息(除了与回复消息间隔数)
        Message message = messageDao.saveByUidAndChatMessageReq(uid, chatMessageReq);

        // 4. 保存与回复消息间隔数
        Long roomId = chatMessageReq.getRoomId();
        Long messageId = message.getId();
        Long replyMessageId = chatMessageReq.getReplyMessageId();
        messageDao.saveGapCount(roomId, messageId, replyMessageId);

        // 5. 让各自的消息处理器保存消息
        messageService.save(message, chatMessageReq);

        // 6. 构建消息响应体
        ChatMessageResp chatMessageResp = messageService.buildChatMessageResp(message, chatMessageReq);
        return ResultUtil.success(chatMessageResp);
    }
}
