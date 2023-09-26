package com.kkuil.blackchat.service.impl;

import com.kkuil.blackchat.exception.UnAuthorizationException;
import com.kkuil.blackchat.service.LoginService;
import com.kkuil.blackchat.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.kkuil.blackchat.constant.UserConst.USER_TOKEN_SECRET;

/**
 * @Author Kkuil
 * @Date 2023/9/26 9:01
 * @Description 登录业务实现类
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    /**
     * 获取用户ID
     *
     * @param token token
     * @return 用户ID
     */
    @Override
    public Long getValidUid(String token) {
        Claims userInfo = JwtUtil.parse(token, USER_TOKEN_SECRET);
        try {
            return (Long) userInfo.get("uid");
        } catch (Exception e) {
            throw new UnAuthorizationException("令牌无效或失效");
        }
    }
}
