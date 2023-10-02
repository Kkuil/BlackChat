package com.kkuil.blackchat.service;

import com.kkuil.blackchat.utils.ResultUtil;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description 针对表【user(用户表)】的数据库操作Service
 */
public interface UserService {

    /**
     * 用户注册
     *
     * @param openId openid
     */
    void register(String openId);

    /**
     * 更新用户名
     *
     * @param uid 用户ID
     * @param username 用户名
     * @return 是否更改成功
     */
    Boolean updateUsername(Long uid, String username);
}
