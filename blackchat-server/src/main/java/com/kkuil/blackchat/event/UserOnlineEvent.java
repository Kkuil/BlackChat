package com.kkuil.blackchat.event;

import com.kkuil.blackchat.event.domain.dto.UserOnlineEventParamsDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @Author Kkuil
 * @Date 2023/9/26 16:32
 * @Description 用户上线事件
 */
@Getter
public class UserOnlineEvent extends ApplicationEvent {

    private final UserOnlineEventParamsDTO userOnlineEventParamsDTO;

    public UserOnlineEvent(Object source, UserOnlineEventParamsDTO userOnlineEventParamsDTO) {
        super(source);
        this.userOnlineEventParamsDTO = userOnlineEventParamsDTO;
    }
}
