package com.kkuil.blackchat.aspect;

import cn.hutool.core.util.ObjectUtil;
import com.kkuil.blackchat.exception.UnAuthorizationException;
import com.kkuil.blackchat.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.kkuil.blackchat.constant.GlobalConst.EMPTY_STR;
import static com.kkuil.blackchat.constant.UserConst.TOKEN_KEY_IN_HEADER;
import static com.kkuil.blackchat.constant.UserConst.USER_TOKEN_SECRET;

/**
 * @Author Kkuil
 * @Date 2023/9/4 9:54
 * @Description 权限切面类
 */
@Aspect
@Component
public class AuthLoginAspect {

    @Pointcut("@annotation(com.kkuil.blackchat.anotations.AuthLogin)")
    public void authLoginAspect() {
    }

    @Around("authLoginAspect()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String token = request.getHeader(TOKEN_KEY_IN_HEADER);
        if (ObjectUtil.isEmpty(token)) {
            throw new UnAuthorizationException();
        }
        try {
            // 验签
            Claims parseToken = JwtUtils.parse(token, USER_TOKEN_SECRET);
            String username = parseToken.get("username").toString();
            if (EMPTY_STR.equals(username)) {
                throw new UnAuthorizationException();
            }
        } catch (Exception e) {
            throw new UnAuthorizationException("token已失效或无效");
        }
        return point.proceed();
    }
}
