package com.kkuil.blackchat.core.mq.constant;

/**
 * @Author Kkuil
 * @Date 2023/10/6 9:26
 * @Description mq常量类
 */
public interface MqConstant {

    /**
     * 消息发送mq
     */
    String SEND_MSG_TOPIC = "chat_send_msg";
    String SEND_MSG_GROUP = "chat_send_msg_group";

    /**
     * push用户
     */
    String PUSH_TOPIC = "websocket_push";
    String PUSH_GROUP = "websocket_push_group";
}
