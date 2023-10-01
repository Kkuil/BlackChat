package com.kkuil.blackchat.web.websocket.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author Kkuil
 * @Date 2023/9/18
 * @Description websocket返回类型枚举
 */
@AllArgsConstructor
@Getter
public enum WsResponseTypeEnum {
    /**
     * 连接成功
     */
    CONN_SUCCESS(0, "连接成功", String.class),

    /**
     * 登录二维码返回
     */
    LOGIN_URL(1, "登录二维码返回", String.class),

    /**
     * 用户扫描成功等待授权
     */
    LOGIN_SUBSCRIBE_SUCCESS(2, "用户订阅成功等待授权", null),

    /**
     * 用户登录成功返回用户信息
     */
    LOGIN_SUCCESS(3, "用户登录成功返回用户信息", String.class),

    /**
     * 新消息
     */
    MESSAGE(4, "新消息", String.class),

    /**
     * 上下线通知
     */
    ONLINE_OFFLINE_NOTIFY(5, "上下线通知", String.class),

    /**
     * 使前端的token失效，意味着前端需要重新登录
     */
    INVALIDATE_TOKEN(6, "使前端的token失效，意味着前端需要重新登录", null),

    /**
     * 拉黑用户
     */
    BLACK(7, "拉黑用户", String.class),

    /**
     * 消息标记
     */
    MARK(8, "消息标记", String.class),

    /**
     * 消息撤回
     */
    RECALL(9, "消息撤回", String.class),

    /**
     * 好友申请
     */
    APPLY(10, "好友申请", String.class),

    /**
     * 成员变动
     */
    MEMBER_CHANGE(11, "成员变动", String.class),

    /**
     * 更新上线列表
     */
    UPDATE_ONLINE_LIST(12, "更新上线列表", List.class),
    ;

    private final Integer type;
    private final String desc;
    private final Class<?> dataClass;

    private static final Map<Integer, WsResponseTypeEnum> CACHE;

    static {
        CACHE = Arrays.stream(WsResponseTypeEnum.values()).collect(Collectors.toMap(WsResponseTypeEnum::getType, Function.identity()));
    }

    public static WsResponseTypeEnum of(Integer type) {
        return CACHE.get(type);
    }
}
