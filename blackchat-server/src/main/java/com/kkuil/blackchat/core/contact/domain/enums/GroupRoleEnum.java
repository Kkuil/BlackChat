package com.kkuil.blackchat.core.contact.domain.enums;

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
public enum GroupRoleEnum {
    /**
     *
     */
    MASTER(1, "群主"),
    ADMIN(2, "管理员"),
    MEMBER(3, "成员"),
    ;

    private final Integer id;
    private final String desc;

    public static final Map<Integer, GroupRoleEnum> CACHE;

    static {
        CACHE = Arrays.stream(GroupRoleEnum.values()).collect(Collectors.toMap(GroupRoleEnum::getId, Function.identity()));
    }

    public static GroupRoleEnum of(Integer type) {
        return CACHE.get(type);
    }
}
