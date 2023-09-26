package com.kkuil.blackchat.interceptors;

import com.kkuil.blackchat.service.LoginService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;
import java.util.Optional;

import static com.kkuil.blackchat.constant.UserConst.*;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description token拦截器
 */
@Order(-2)
@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Resource
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取用户登录token
        String token = getToken(request);
        Long uid = loginService.getValidUid(token);
        // 有登录态
        if (Objects.nonNull(uid)) {
            request.setAttribute(ATTRIBUTE_UID_IN_HEADER, uid);
            return true;
        }
        return false;
    }

    private String getToken(HttpServletRequest request) {
        String header = request.getHeader(TOKEN_KEY_IN_HEADER);
        return Optional.ofNullable(header)
                .filter(h -> h.startsWith(TOKEN_KEY_IN_HEADER_PREFIX))
                .map(h -> h.substring(TOKEN_KEY_IN_HEADER_PREFIX.length()))
                .orElse(null);
    }
}
