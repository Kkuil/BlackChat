package com.kkuil.blackchat.service;

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

}
