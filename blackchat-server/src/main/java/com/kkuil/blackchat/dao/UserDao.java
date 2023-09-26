package com.kkuil.blackchat.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.User;
import com.kkuil.blackchat.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * @Author Kkuil
 * @Date 2023/9/26 15:55
 * @Description 用户访问层
 */
@Service
public class UserDAO extends ServiceImpl<UserMapper, User> {

    /**
     * 通过openid获取用户
     *
     * @param openId openid
     * @return 用户信息
     */
    public User getByOpenId(String openId) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("open_id", openId);
        return getOne(wrapper);
    }

}
