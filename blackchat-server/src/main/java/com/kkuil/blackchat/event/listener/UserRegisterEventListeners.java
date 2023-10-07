package com.kkuil.blackchat.event.listener;

import com.kkuil.blackchat.domain.entity.User;
import com.kkuil.blackchat.domain.enums.IdempotentEnum;
import com.kkuil.blackchat.domain.enums.user.ItemEnum;
import com.kkuil.blackchat.event.UserRegisterEvent;
import com.kkuil.blackchat.service.UserBackpackService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Author Kkuil
 * @Date 2023/9/27 9:44
 * @Description 用户注册事件监听器
 */
@Component
@Slf4j
public class UserRegisterEventListeners {

    @Resource
    private UserBackpackService userBackpackService;

    /**
     * 发送给用户注册成功，送一张改名卡
     *
     * @param event 用户注册事件
     */
    @Async
    @EventListener(classes = UserRegisterEvent.class)
    public void addUpdateNameCard(UserRegisterEvent event) {
        User user = event.getUser();
        //送一张改名卡
        userBackpackService.acquireItem(user.getId(), ItemEnum.MODIFY_NAME_CARD.getId(), IdempotentEnum.UID, user.getId().toString());
    }
}
