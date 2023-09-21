package com.kkuil.blackchat.exception;

/**
 * @Author Kkuil
 * @Date 2023/9/4 10:43
 * @Description 未登录异常
 */
public class UnAuthorizationException extends RuntimeException {

    public UnAuthorizationException() {
    }

    public UnAuthorizationException(String message) {
        super(message);
    }
}
