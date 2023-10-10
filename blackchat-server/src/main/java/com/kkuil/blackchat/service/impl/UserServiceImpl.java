package com.kkuil.blackchat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.kkuil.blackchat.cache.UserCache;
import com.kkuil.blackchat.core.user.domain.vo.request.UserInfoCache;
import com.kkuil.blackchat.dao.UserBackpackDAO;
import com.kkuil.blackchat.dao.UserDAO;
import com.kkuil.blackchat.domain.entity.User;
import com.kkuil.blackchat.domain.entity.UserBackpack;
import com.kkuil.blackchat.domain.enums.UserMessageEnum;
import com.kkuil.blackchat.domain.enums.error.CommonErrorEnum;
import com.kkuil.blackchat.domain.enums.error.UserErrorEnum;
import com.kkuil.blackchat.domain.enums.user.ItemTypeEnum;
import com.kkuil.blackchat.event.UserRegisterEvent;
import com.kkuil.blackchat.service.UserService;
import com.kkuil.blackchat.utils.AssertUtil;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description 针对表【user(用户表)】的数据库操作Service实现
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserCache userCache;

    @Resource
    private UserDAO userDao;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Resource
    private UserBackpackDAO userBackpackDao;

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

    /**
     * 更新用户名
     *
     * @param uid      用户ID
     * @param username 用户名
     * @return 是否更改成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUsername(Long uid, String username) {
        // 1. 先判断用户是否拥有改名卡
        Long updateNameCard = Long.parseLong(ItemTypeEnum.MODIFY_NAME_CARD.getType().toString());
        List<UserBackpack> validByItemIds = userBackpackDao.getValidByItemIds(uid, Collections.singletonList(updateNameCard));
        boolean isOwned = CollectionUtil.isNotEmpty(validByItemIds);
        AssertUtil.isTrue(isOwned, UserErrorEnum.NOT_ENOUGH_UPDATE_NAME_COUNT.getMsg());

        // 2. 判断用户名是否被使用
        User user = userDao.getByUsername(username);
        AssertUtil.isEmpty(user, UserErrorEnum.USERNAME_IS_USED.getMsg());

        // 3. 更改用户名
        return userDao.updateUsername(uid, username);
    }

    /**
     * 批量获取用户信息
     *
     * @param uidList 用户ID列表
     * @return 用户缓存列表
     */
    @Override
    public List<UserInfoCache> getBatchUserInfoCache(List<Long> uidList) {
        return userCache.getBatch(uidList);
    }

}
