package com.kkuil.blackchat.event.listener;

import com.kkuil.blackchat.cache.UserCache;
import com.kkuil.blackchat.dao.UserDAO;
import com.kkuil.blackchat.domain.entity.User;
import com.kkuil.blackchat.event.UserOfflineEvent;
import com.kkuil.blackchat.web.websocket.domain.enums.ChatActiveStatusEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author Kkuil
 * @Date 2023/9/27 9:44
 * @Description 用户下线事件监听器
 */
@Component
@Slf4j
public class UserOfflineEventListeners {

    @Resource
    private UserDAO userDao;

    @Resource
    private UserCache userCache;

    /**
     * 更新缓存层数据库
     *
     * @param event 用户下线事件参数
     */
    @Async
    @EventListener(classes = UserOfflineEvent.class)
    public void updateCacheDB(UserOfflineEvent event) {
        Long uid = event.getUid();
        // 在redis缓存中删除那个uid
        userCache.offline(uid, new Date());
    }

    /**
     * 更新持久层数据库
     *
     * @param event 用户下线事件参数
     */
    @Async
    @EventListener(classes = UserOfflineEvent.class)
    public void updatePersistenceDB(UserOfflineEvent event) {
        Long uid = event.getUid();
        User user = new User();
        user.setId(uid);
        user.setLastOptTime(new Date());
        user.setActiveStatus(ChatActiveStatusEnum.OFFLINE.getStatus());
        userDao.updateById(user);
    }
}
