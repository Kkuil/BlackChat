package com.kkuil.blackchat.service;

/**
 * @Author Kkuil
 * @Date 2023/9/26 8:59
 * @Description 登录业务接口
 */
public interface LoginService {

    /**
     * 获取用户ID
     *
     * @param token token
     * @return 用户ID
     */
    Long getValidUid(String token);

}
