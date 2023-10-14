package com.kkuil.blackchat.event.listener;

import com.kkuil.blackchat.core.mq.MqProducer;
import com.kkuil.blackchat.core.mq.constant.MqConstant;
import com.kkuil.blackchat.dao.ContactDAO;
import com.kkuil.blackchat.dao.RoomDAO;
import com.kkuil.blackchat.domain.entity.Message;
import com.kkuil.blackchat.event.MessageSendEvent;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Date;

/**
 * @Author Kkuil
 * @Date 2023/10/6 9:41
 * @Description 消息发送监听器
 */
@Slf4j
@Component
public class MessageSendListener {

    @Resource
    private MqProducer mqProducer;

    @Resource
    private RoomDAO roomDao;

    @Resource
    private ContactDAO contactDao;

    /**
     * 消息路由
     *
     * @param event 事件
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT, classes = MessageSendEvent.class, fallbackExecution = true)
    public void messageRoute(MessageSendEvent event) {
        Message message = event.getMessage();
        mqProducer.sendMessage(MqConstant.SEND_MSG_TOPIC, message.getId());
    }

    /**
     * 消息路由
     *
     * @param event 事件
     */
    @Async
    @EventListener(classes = MessageSendEvent.class)
    public void updateMessage(MessageSendEvent event) {
        // 更新每个房间的最新消息时间（active_time）和最新消息ID (last_msg_id)
        Message message = event.getMessage();
        Long roomId = message.getRoomId();
        Date updateTime = message.getUpdateTime();
        roomDao.updateRoomNewestMsg(roomId, updateTime, message.getId());
        contactDao.updateReadTime(roomId, updateTime);
        log.info("消息发送成功，消息ID：{}", message.getId());
    }

    // @TransactionalEventListener(classes = MessageSendEvent.class, fallbackExecution = true)
    // public void publishChatToWechat(@NotNull MessageSendEvent event) {
    //     Message message = messageDao.getById(event.getMsgId());
    //     if (Objects.nonNull(message.getExtra().getAtUidList())) {
    //         weChatMsgOperationService.publishChatMsgToWeChatUser(message.getFromUid(), message.getExtra().getAtUidList(),
    //                 message.getContent());
    //     }
    // }
}
