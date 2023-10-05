package com.kkuil.blackchat.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author Kkuil
 * @Date 2023/9/27
 * @Description 房间类型枚举
 */
@AllArgsConstructor
@Getter
public enum RoomTypeEnum {
    /**
     *
     */
    COMMON(0, "普通群聊"),
    HOT_FLAG(1, "热点群聊"),
    ;

    private final Integer type;
    private final String desc;

    private static final Map<Integer, RoomTypeEnum> CACHE;

    static {
        CACHE = Arrays.stream(RoomTypeEnum.values()).collect(Collectors.toMap(RoomTypeEnum::getType, Function.identity()));
    }

    public static RoomTypeEnum of(Integer type) {
        return CACHE.get(type);
    }
}
