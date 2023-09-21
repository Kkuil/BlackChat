package com.kkuil.blackchat.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

/**
 * @Author Kkuil
 * @Date 2023/9/3 22:02
 * @Description 日志拦截器
 */
@Component
@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = request.getRemoteAddr();
        StringBuffer requestUrl = request.getRequestURL();
        String sessionId = request.getRequestedSessionId();
        Map<String, String[]> parameterMap = request.getParameterMap();

        log.info("ip = {}", ip);
        log.info("requestUrl = {}", requestUrl);
        log.info("sessionId = {}", sessionId);
        log.info("pathInfo = {}", request.getPathInfo());
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            log.info("entry = {}", entry);
        }
        return true;
    }
}
