package com.kkuil.blackchat.service.impl;

import cn.hutool.core.util.StrUtil;
import com.kkuil.blackchat.constant.RedisKey;
import com.kkuil.blackchat.exception.UnAuthorizationException;
import com.kkuil.blackchat.service.LoginService;
import com.kkuil.blackchat.utils.JwtUtil;
import com.kkuil.blackchat.utils.RedisUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static com.kkuil.blackchat.constant.UserConst.USER_TOKEN_SECRET;
import static com.kkuil.blackchat.constant.UserConst.USER_TOKEN_TTL;

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

    /**
     * 登录
     *
     * @param uid 用户ID
     * @return token
     */
    @Override
    public String login(Long uid) {
        String key = RedisKey.getKey(RedisKey.USER_TOKEN_STRING, uid);
        // 获取用户token
        String token = RedisUtil.getStr(key);
        if (StrUtil.isNotBlank(token)) {
            return token;
        }
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("uid", uid);
        token = JwtUtil.create(map, USER_TOKEN_SECRET, USER_TOKEN_TTL);
        return token;
    }
}
