package com.kkuil.blackchat.interceptors;

import com.kkuil.blackchat.web.websocket.constant.AuthorizationConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @Author kkuil
 * @Date 2023/07/29 20:00
 * @Description 登录拦截器
 */
public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return true;
    }
}
