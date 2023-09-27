package com.kkuil.blackchat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author Kkuil
 * @Date 2023/9/27
 * @Description 是否判断
 */
@AllArgsConstructor
@Getter
public enum YesOrNoEnum {
    /**
     * 否
     */
    NO(0, "否"),

    /**
     * 是
     */
    YES(1, "是"),
    ;

    private final Integer status;
    private final String desc;

    private static final Map<Integer, YesOrNoEnum> CACHE;

    static {
        CACHE = Arrays.stream(YesOrNoEnum.values()).collect(Collectors.toMap(YesOrNoEnum::getStatus, Function.identity()));
    }

    public static YesOrNoEnum of(Integer type) {
        return CACHE.get(type);
    }

    public static Integer toStatus(Boolean bool) {
        return bool ? YES.getStatus() : NO.getStatus();
    }
}
