package com.kkuil.blackchat.constant;

/**
 * @Author 小K
 * @Date 2023/08/03 23:00
 * @Description 用户常量
 */
public class UserConst {
    /**
     * 用户token密钥
     */
    public static final String USER_TOKEN_SECRET = "blackchat";

    /**
     * 用户token过期时间
     */
    public static final int USER_TOKEN_TTL = 60 * 30 * 60 * 1000;

    /**
     * 用户token刷新时间
     */
    public static final int USER_TOKEN_REFRESH_TIME = 10 * 60 * 1000;

    /**
     * 用户请求响应头中token的key
     */
    public static final String TOKEN_KEY_IN_HEADER = "Authorization";

    /**
     * 请求头 / 响应头中的token的前缀
     */
    public static final String TOKEN_KEY_IN_HEADER_PREFIX = "Bearer ";

    /**
     * 存在请求头中的用户ID
     */
    public static final String ATTRIBUTE_UID_IN_HEADER = "uid";

    /**
     * 用户加密盐值
     */
    public static final String USER_ENCRYPT_VALUE = "kkuil";
}
