package com.kkuil.blackchat.core.mq;

import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @Author Kkuil
 * @Date 2023/10/6 9:23
 * @Description 发送mq工具类
 */
@Component
public class MqProducer {

    @Resource
    private RocketMQTemplate rocketMqTemplate;

    /**
     * 发送消息
     *
     * @param topic 发送消息的通道名
     * @param body  消息体
     */
    public void sendMessage(String topic, Object body) {
        Message<Object> build = MessageBuilder.withPayload(body).build();
        rocketMqTemplate.send(topic, build);
    }

    // /**
    //  * 发送可靠消息，在事务提交后保证发送成功
    //  *
    //  * @param topic
    //  * @param body
    //  */
    // @SecureInvoke
    // public void sendSecureMsg(String topic, Object body, Object key) {
    //     Message<Object> build = MessageBuilder
    //             .withPayload(body)
    //             .setHeader("KEYS", key)
    //             .build();
    //     rocketMqTemplate.send(topic, build);
    // }
}
