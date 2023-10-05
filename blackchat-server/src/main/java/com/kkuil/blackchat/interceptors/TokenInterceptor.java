package com.kkuil.blackchat.interceptors;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.kkuil.blackchat.dao.RoomDAO;
import com.kkuil.blackchat.domain.entity.Room;
import com.kkuil.blackchat.domain.enums.RoomTypeEnum;
import com.kkuil.blackchat.exception.UnAuthorizationException;
import com.kkuil.blackchat.service.LoginService;
import com.kkuil.blackchat.utils.AssertUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;
import java.util.Optional;

import static com.kkuil.blackchat.constant.RoomConst.HOT_ROOM_ID;
import static com.kkuil.blackchat.constant.UserConst.*;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description token拦截器
 */
@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    private static final LoginService loginService;

    static {
        loginService = SpringUtil.getBean(LoginService.class);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取用户登录token
        String token = getToken(request);
        Long uid = loginService.getValidUid(token);
        // 有登录态
        if (Objects.isNull(uid)) {
            // 判断是不是大群聊
            String roomId = request.getParameterMap().get("roomId")[0];
            if (StrUtil.isBlank(roomId)) {
                throw new UnAuthorizationException("请先登录后再进行操作吧~");
            }
            return HOT_ROOM_ID.equals(Long.parseLong(roomId));
        }
        request.setAttribute(ATTRIBUTE_UID_IN_HEADER, uid);
        return true;
    }

    private String getToken(HttpServletRequest request) {
        String header = request.getHeader(TOKEN_KEY_IN_HEADER);
        return Optional.ofNullable(header)
                .filter(h -> h.startsWith(TOKEN_KEY_IN_HEADER_PREFIX))
                .map(h -> h.substring(TOKEN_KEY_IN_HEADER_PREFIX.length()))
                .orElse("");
    }
}
