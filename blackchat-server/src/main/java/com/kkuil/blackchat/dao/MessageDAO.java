package com.kkuil.blackchat.dao;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.Message;
import com.kkuil.blackchat.mapper.MessageMapper;
import com.kkuil.blackchat.web.chat.domain.enums.MessageStatusEnum;
import com.kkuil.blackchat.web.websocket.domain.dto.chat.AbstractChatMessageBaseReq;
import com.kkuil.blackchat.web.websocket.domain.vo.request.ChatMessageReq;
import org.springframework.stereotype.Service;

/**
 * @Author Kkuil
 * @Date 2023/9/28 18:28
 * @Description 消息访问层
 */
@Service
public class MessageDAO extends ServiceImpl<MessageMapper, Message> {

    /**
     * 通过用户ID和chatMessageReq保存消息
     *
     * @param uid            用户ID
     * @param chatMessageReq 消息体
     * @return 消息
     */
    public Message saveByUidAndChatMessageReq(Long uid, ChatMessageReq<? extends AbstractChatMessageBaseReq> chatMessageReq) {
        Message message = Message.builder()
                .fromUid(uid)
                .roomId(chatMessageReq.getRoomId())
                .type(chatMessageReq.getMessageType())
                .replyMessageId(chatMessageReq.getReplyMessageId())
                .status(MessageStatusEnum.NORMAL.getStatus())
                .build();
        this.save(message);
        return message;
    }

    /**
     * 计算同一房间内两条消息的间隔数
     *
     * @param roomId 房间号
     * @param fromId 原消息
     * @param toId   定位消息
     */
    public void saveGapCount(Long roomId, Long fromId, Long toId) {
        Long gapCount = lambdaQuery()
                .eq(Message::getRoomId, roomId)
                .gt(Message::getId, fromId)
                .le(Message::getId, toId)
                .count();
        UpdateWrapper<Message> wrapper = new UpdateWrapper<>();
        wrapper
                .eq(true, "id", fromId)
                .set(true, "gap_count", gapCount);
        this.update(wrapper);
    }
}
