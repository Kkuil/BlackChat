package com.kkuil.blackchat.web.websocket.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author Kkuil
 * @Date 2023/9/18
 * @Description websocket请求类型枚举类
 */
@AllArgsConstructor
@Getter
public enum WsRequestTypeEnum {
    /**
     * 请求登录二维码
     */
    LOGIN(1001, "请求登录二维码"),

    /**
     * 心跳包
     */
    HEARTBEAT(1002, "心跳包"),

    /**
     * 登录认证
     */
    AUTHORIZE(1003, "登录认证"),

    /**
     * 退出登录
     */
    LOGOUT(1004, "退出登录"),
    ;

    private final Integer type;
    private final String desc;

    public static final Map<Integer, WsRequestTypeEnum> WS_REQUEST_TYPE_CACHE;

    static {
        WS_REQUEST_TYPE_CACHE = Arrays
                .stream(WsRequestTypeEnum.values())
                .collect(
                        Collectors.toMap(
                                WsRequestTypeEnum::getType,
                                Function.identity()
                        )
                );
    }

    public static WsRequestTypeEnum of(Integer type) {
        return WS_REQUEST_TYPE_CACHE.get(type);
    }
}
