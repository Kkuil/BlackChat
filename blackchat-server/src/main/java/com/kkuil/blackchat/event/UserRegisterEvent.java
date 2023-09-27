package com.kkuil.blackchat.event;

import com.kkuil.blackchat.domain.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @Author Kkuil
 * @Date 2023/9/26 16:32
 * @Description 用户注册事件
 */
@Getter
public class UserRegisterEvent extends ApplicationEvent {
    private final User user;

    public UserRegisterEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
