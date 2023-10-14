package com.kkuil.blackchat.core.chat.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author Kkuil
 * @Date 2023/9/29 17:21
 * @Description 消息类型枚举
 */
@AllArgsConstructor
@Getter
public enum MessageTypeEnum {
    /**
     *
     */
    TEXT(1, "正常消息"),
    REVOKE(2, "撤回消息"),
    IMG(3, "图片"),
    FILE(4, "文件"),
    SOUND(5, "语音"),
    VIDEO(6, "视频"),
    SYSTEM(7, "系统消息"),
    ;

    private final Integer type;
    private final String desc;

    private static final Map<Integer, MessageTypeEnum> CACHE;

    static {
        CACHE = Arrays.stream(MessageTypeEnum.values()).collect(Collectors.toMap(MessageTypeEnum::getType, Function.identity()));
    }

    public static MessageTypeEnum of(Integer type) {
        return CACHE.get(type);
    }
}
