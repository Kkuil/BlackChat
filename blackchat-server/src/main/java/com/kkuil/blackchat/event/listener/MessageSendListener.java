package com.kkuil.blackchat.event.listener;

import com.kkuil.blackchat.core.mq.MqProducer;
import com.kkuil.blackchat.core.mq.constant.MqConstant;
import com.kkuil.blackchat.event.MessageSendEvent;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
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

    // @TransactionalEventListener(classes = MessageSendEvent.class, fallbackExecution = true)
    // public void publishChatToWechat(@NotNull MessageSendEvent event) {
    //     Message message = messageDao.getById(event.getMsgId());
    //     if (Objects.nonNull(message.getExtra().getAtUidList())) {
    //         weChatMsgOperationService.publishChatMsgToWeChatUser(message.getFromUid(), message.getExtra().getAtUidList(),
    //                 message.getContent());
    //     }
    // }
}
