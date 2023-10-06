package com.kkuil.blackchat.core.websocket.constant;

import io.netty.util.AttributeKey;

/**
 * @Author Kkuil
 * @Date 2023/9/21 12:48
 * @Description 权限常量
 */
public class AuthorizationConst {

    /**
     * 通道中上下文传递的token键名
     */
    public static final AttributeKey<String> TOKEN_KEY_IN_CHANNEL = AttributeKey.valueOf("Authorization");

    /**
     * 通道中上下文传递的ip键名
     */
    public static final AttributeKey<String> IP_KEY_IN_CHANNEL = AttributeKey.valueOf("ip");

    /**
     * 通道中上下文传递的用户ID键名
     */
    public static final AttributeKey<Long> UID_KEY_IN_CHANNEL = AttributeKey.valueOf("uid");
}
