package com.kkuil.blackchat.dao;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.cache.UserCache;
import com.kkuil.blackchat.domain.entity.User;
import com.kkuil.blackchat.domain.enums.YesOrNoEnum;
import com.kkuil.blackchat.domain.enums.user.ItemTypeEnum;
import com.kkuil.blackchat.domain.vo.response.CursorPageBaseResp;
import com.kkuil.blackchat.mapper.UserMapper;
import com.kkuil.blackchat.utils.CursorUtil;
import com.kkuil.blackchat.core.chat.domain.vo.request.member.ChatMemberCursorReq;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/9/26 15:55
 * @Description 用户访问层
 */
@Service
public class UserDAO extends ServiceImpl<UserMapper, User> {

    @Resource
    private UserBackpackDAO userBackpackDao;

    @Resource
    private UserCache userCache;

    /**
     * 通过openid获取用户
     *
     * @param openId openid
     * @return 用户信息
     */
    public User getByOpenId(String openId) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getOpenId, openId);
        return this.getOne(wrapper);
    }

    /**
     * 通过用户名获取用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    public User getByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getName, username);
        return getOne(wrapper);
    }

    /**
     * 游标获取数据
     *
     * @param uidList uidList
     * @param request 游标请求
     * @return 数据
     */
    public CursorPageBaseResp<User> getCursorPage(List<Long> uidList, ChatMemberCursorReq request) {
        return CursorUtil.getCursorPageByMysql(this, request, wrapper -> {
            wrapper.eq(User::getActiveStatus, request.getActiveStatus());
            wrapper.in(CollectionUtil.isNotEmpty(uidList), User::getId, uidList);
        }, User::getLastOptTime);
    }

    /**
     * 更改用户名
     *
     * @param uid      用户ID
     * @param username 用户名
     * @return 是否更名成功
     */
    public Boolean updateUsername(Long uid, String username) {
        // 1. 使用改名卡
        userBackpackDao.updateStatus(uid, ItemTypeEnum.MODIFY_NAME_CARD.getType(), YesOrNoEnum.YES.getStatus());

        // 2. 更改名字
        User user = new User();
        user.setId(uid);
        user.setName(username);

        // 3. 更新缓存
        userCache.updateUserInfo(user);

        return this.updateById(user);
    }
}
