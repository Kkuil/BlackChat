package com.kkuil.blackchat.domain.dto;

import lombok.ToString;

/**
 * @Author Kkuil
 * @Date 2023/9/26 8:16
 * @Description 请求参数DTO
 */
@ToString
public class RequestHolderDTO {
    private static final ThreadLocal<RequestInfo> THREAD_LOCAL = new ThreadLocal<>();

    public static void set(RequestInfo requestInfo) {
        THREAD_LOCAL.set(requestInfo);
    }

    public static RequestInfo get() {
        return THREAD_LOCAL.get();
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

}
