package com.kkuil.blackchat.event;

import com.kkuil.blackchat.model.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @Author Kkuil
 * @Date 2023/9/17 11:51
 * @Description 用户注册事件
 */
@Getter
public class UserRegisterEvent extends ApplicationEvent {

    private User user;

    public UserRegisterEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
