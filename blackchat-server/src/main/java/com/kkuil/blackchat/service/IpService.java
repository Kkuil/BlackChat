package com.kkuil.blackchat.service;

/**
 * @Author Kkuil
 * @Date 2023/10/4 23:34
 * @Description ip服务类接口
 */
public interface IpService {

    /**
     * 异步更新用户ip详情
     *
     * @param uid 用户ID
     */
    void refreshIpDetailAsync(Long uid);
}
