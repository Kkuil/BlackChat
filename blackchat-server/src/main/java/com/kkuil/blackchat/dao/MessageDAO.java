package com.kkuil.blackchat.dao;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.Message;
import com.kkuil.blackchat.domain.vo.response.CursorPageBaseResp;
import com.kkuil.blackchat.mapper.MessageMapper;
import com.kkuil.blackchat.utils.CursorUtil;
import com.kkuil.blackchat.core.chat.domain.enums.MessageStatusEnum;
import com.kkuil.blackchat.core.chat.domain.vo.request.message.ChatMessageCursorReq;
import com.kkuil.blackchat.core.websocket.domain.vo.request.ChatMessageReq;
import org.springframework.stereotype.Service;

import java.util.Date;

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
    public Message saveByUidAndChatMessageReq(Long uid, ChatMessageReq chatMessageReq) {
        Message message = Message.builder()
                .fromUid(uid)
                .roomId(chatMessageReq.getRoomId())
                .type(chatMessageReq.getMessageType())
                .replyMessageId(chatMessageReq.getReplyMessageId())
                .status(MessageStatusEnum.NORMAL.getStatus())
                .build();
        this.save(message);
        return this.getById(message.getId());
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
                .lambda()
                .eq(true, Message::getId, toId)
                .set(true, Message::getGapCount, gapCount);
        this.update(wrapper);
    }

    /**
     * 获取消息列表
     *
     * @param request 消息请求
     * @return 消息列表
     */
    public CursorPageBaseResp<Message> getCursorPage(ChatMessageCursorReq request) {
        return CursorUtil.getCursorPageByMysql(this, request, wrapper -> {
            wrapper.eq(Message::getStatus, MessageStatusEnum.NORMAL.getStatus());
            wrapper.eq(Message::getRoomId, request.getRoomId());
        }, Message::getCreateTime);
    }

    /**
     * 获取未读数
     *
     * @param roomId   房间ID
     * @param readTime 最后已读时间
     * @return 未读数量
     */
    public Integer getUnReadCountByReadTime(Long roomId, Date readTime) {
        return Math.toIntExact(lambdaQuery()
                .eq(Message::getRoomId, roomId)
                .gt(ObjectUtil.isNotNull(readTime), Message::getCreateTime, readTime)
                .count());
    }

    /**
     * 删除某用户在某个会话中的消息记录
     *
     * @param roomId 房间ID
     * @param uid 用户ID
     * @return 是否删除
     */
    public Boolean delRecordBatch(Long roomId, Long uid) {
        QueryWrapper<Message> wrapper = new QueryWrapper<Message>()
                .eq("room_id", roomId)
                .eq("uid", uid);
        return this.remove(wrapper);
    }
}
