package com.kkuil.blackchat.core.mq;

import com.kkuil.blackchat.core.mq.constant.MqConstant;
import com.kkuil.blackchat.core.mq.domain.dto.PushMessageDTO;
import com.kkuil.blackchat.core.mq.domain.enums.WsPushTypeEnum;
import com.kkuil.blackchat.core.websocket.service.WebSocketService;
import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @Author Kkuil
 * @Date 2023/10/6 9:26
 * @Description 消费消息
 */
@RocketMQMessageListener(topic = MqConstant.PUSH_TOPIC, consumerGroup = MqConstant.PUSH_GROUP, messageModel = MessageModel.BROADCASTING)
@Component
public class PushConsumer implements RocketMQListener<PushMessageDTO> {
    @Resource
    private WebSocketService webSocketService;

    /**
     * 监听消息
     *
     * @param message 消息
     */
    @Override
    public void onMessage(PushMessageDTO message) {
        WsPushTypeEnum wsPushTypeEnum = WsPushTypeEnum.of(message.getPushType());
        switch (wsPushTypeEnum) {
            case USER -> webSocketService.sendMsgToOne(message.getUid(), message.getWsBaseMsg());
            case ALL -> webSocketService.sendMsgToAll(message.getWsBaseMsg(), null);
            default -> {
                return;
            }
        }
    }
}
