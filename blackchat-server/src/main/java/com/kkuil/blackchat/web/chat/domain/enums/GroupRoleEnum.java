package com.kkuil.blackchat.web.chat.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author Kkuil
 * @Date 2023/9/28 20:30
 * @Description 群成员角色枚举
 */
@AllArgsConstructor
@Getter
public enum GroupRoleEnum {
    /**
     *
     */
    LEADER(1, "群主"),
    MANAGER(2, "管理"),
    MEMBER(3, "普通成员"),
    ;

    private final Integer type;
    private final String desc;

    private static Map<Integer, GroupRoleEnum> cache;

    static {
        cache = Arrays.stream(GroupRoleEnum.values()).collect(Collectors.toMap(GroupRoleEnum::getType, Function.identity()));
    }

    public static GroupRoleEnum of(Integer type) {
        return cache.get(type);
    }
}
