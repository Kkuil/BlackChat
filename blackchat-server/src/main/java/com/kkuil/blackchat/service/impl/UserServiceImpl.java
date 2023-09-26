package com.kkuil.blackchat.service.impl;

import com.kkuil.blackchat.dao.UserDAO;
import com.kkuil.blackchat.domain.entity.User;
import com.kkuil.blackchat.event.UserRegisterEvent;
import com.kkuil.blackchat.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description 针对表【user(用户表)】的数据库操作Service实现
 */
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private UserDAO userDao;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * 注册
     *
     * @param openId openid
     */
    @Override
    public void register(String openId) {
        User user = User.builder().openId(openId).build();
        userDao.save(user);
        applicationEventPublisher.publishEvent(new UserRegisterEvent(this, user));
    }
}
