package com.kkuil.blackchat.event;

import com.kkuil.blackchat.domain.entity.Message;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @Author Kkuil
 * @Date 2023/10/6 9:37
 * @Description 消息发送事件
 */
@Getter
public class MessageSendEvent extends ApplicationEvent {
    private final Message message;

    public MessageSendEvent(Object source, Message message) {
        super(source);
        this.message = message;
    }
}
