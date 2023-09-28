package com.kkuil.blackchat.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author Kkuil
 * @Date 2023/9/28 22:23
 * @Description 角色枚举
 */
@AllArgsConstructor
@Getter
public enum RoleEnum {
    /**
     *
     */
    ADMIN(1L, "理超级管理员"),
    CHAT_MANAGER(2L, "blackchat群聊管"),
    ;

    private final Long id;
    private final String desc;

    public static final Map<Long, RoleEnum> CACHE;

    static {
        CACHE = Arrays.stream(RoleEnum.values()).collect(Collectors.toMap(RoleEnum::getId, Function.identity()));
    }

    public static RoleEnum of(Long type) {
        return CACHE.get(type);
    }
}
