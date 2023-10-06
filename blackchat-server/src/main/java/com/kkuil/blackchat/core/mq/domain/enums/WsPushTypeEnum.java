package com.kkuil.blackchat.core.mq.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author Kkuil
 * @Date 2023/10/6 9:28
 * @Description 消息推送枚举类
 */
@AllArgsConstructor
@Getter
public enum WsPushTypeEnum {
    /**
     *
     */
    USER(1, "个人"),
    ALL(2, "全部连接用户"),
    ;

    private final Integer type;
    private final String desc;

    private static final Map<Integer, WsPushTypeEnum> CACHE;

    static {
        CACHE = Arrays.stream(WsPushTypeEnum.values()).collect(Collectors.toMap(WsPushTypeEnum::getType, Function.identity()));
    }

    public static WsPushTypeEnum of(Integer type) {
        return CACHE.get(type);
    }
}
