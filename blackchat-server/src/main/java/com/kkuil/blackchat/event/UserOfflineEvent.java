package com.kkuil.blackchat.event;

import com.kkuil.blackchat.domain.entity.User;
import com.kkuil.blackchat.event.domain.dto.UserOnlineEventParamsDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @Author Kkuil
 * @Date 2023/9/26 16:32
 * @Description 用户下线事件
 */
@Getter
public class UserOfflineEvent extends ApplicationEvent {

    private final Long uid;

    public UserOfflineEvent(Object source, Long uid) {
        super(source);
        this.uid = uid;
    }
}
