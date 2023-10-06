package com.kkuil.blackchat.service.impl;

import cn.hutool.core.util.StrUtil;
import com.kkuil.blackchat.constant.RedisKeyConst;
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

    public static final String NULL = "null";

    /**
     * 获取用户ID
     *
     * @param token token
     * @return 用户ID
     */
    @Override
    public Long getValidUid(String token) {
        if (StrUtil.isEmpty(token) || NULL.equals(token)) {
            return null;
        }
        Claims userInfo = JwtUtil.parse(token, USER_TOKEN_SECRET);
        return Long.parseLong(userInfo.get("uid").toString());
    }

    /**
     * 登录
     *
     * @param uid 用户ID
     * @return token
     */
    @Override
    public String login(Long uid) {
        String key = RedisKeyConst.getKey(RedisKeyConst.USER_TOKEN_STRING, uid);
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

    /**
     * 验证token的有效性
     *
     * @param token token
     * @return 是否有效
     */
    @Override
    public boolean verify(String token) {
        try {
            JwtUtil.parse(token, USER_TOKEN_SECRET);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
