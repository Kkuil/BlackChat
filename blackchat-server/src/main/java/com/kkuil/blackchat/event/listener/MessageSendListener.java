package com.kkuil.blackchat.event.listener;

import com.kkuil.blackchat.core.mq.MqProducer;
import com.kkuil.blackchat.core.mq.constant.MqConstant;
import com.kkuil.blackchat.dao.MessageDAO;
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
    private MessageDAO messageDao;

    /**
     * 消息路由
     *
     * @param event 事件
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT, classes = MessageSendEvent.class, fallbackExecution = true)
    public void messageRoute(MessageSendEvent event) {
        Long msgId = event.getMsgId();
        mqProducer.sendMessage(MqConstant.SEND_MSG_TOPIC, msgId);
    }

    /**
     * 消息路由
     *
     * @param event 事件
     */
    @Async
    @EventListener(classes = MessageSendEvent.class)
    public void updateMessage(MessageSendEvent event) {
        Long msgId = event.getMsgId();
        // 更新每个房间的最新消息时间（active_time）和最新消息ID (last_msg_id)
        Message message = messageDao.getById(msgId);
        roomDao.updateRoomNewestMsg(message.getRoomId(), message.getCreateTime(), message.getId());
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
