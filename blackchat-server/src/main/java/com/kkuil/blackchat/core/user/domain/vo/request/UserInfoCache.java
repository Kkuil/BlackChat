package com.kkuil.blackchat.core.user.domain.vo.request;

import lombok.Data;

/**
 * @Author Kkuil
 * @Date 2023/10/8 18:41
 * @Description 用户信息缓存
 */
@Data
public class UserInfoCache {
    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 用户头像
     */
    private String avatar;
}
