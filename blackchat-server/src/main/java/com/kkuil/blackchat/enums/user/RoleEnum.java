package com.kkuil.blackchat.enums.user;

import lombok.Getter;

/**
 * @Author Kkuil
 * @Date 2023/9/27 10:37
 * @Description 角色枚举类
 */
@Getter
public enum RoleEnum {
    /**
     * 超级管理员
     */
    SUPER_ADMIN(9001),

    /**
     * blackchat管理员
     */
    GROUP_ADMIN(9002),

    /**
     * 普通用户
     */
    USER(9003);


    public final long value;

    RoleEnum(long value) {
        this.value = value;
    }
}
