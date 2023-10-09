package com.kkuil.blackchat.utils;

import com.kkuil.blackchat.exception.UnAuthorizationException;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.Map;

/**
 * @Author Kkuil
 * @Date 2023/08/05 12:30
 * @Description Jwt工具类
 */
public class JwtUtil {
    /**
     * 生成token
     *
     * @param data Map<String, Object>
     * @return String
     */
    public static String create(Map<String, Object> data, String secret) {
        try {
            return Jwts.builder()
                    .signWith(SignatureAlgorithm.HS256, secret)
                    .setClaims(data)
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成token
     *
     * @param data Map<String, Object>
     * @return String
     */
    public static String create(Map<String, Object> data, String secret, long ttl) {
        try {
            return Jwts.builder()
                    .signWith(SignatureAlgorithm.HS256, secret)
                    .setClaims(data)
                    .setExpiration(new Date(System.currentTimeMillis() + ttl))
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析token
     *
     * @param token  token
     * @param secret 密钥
     * @return 解析结果
     */
    public static Claims parse(String token, String secret) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new UnAuthorizationException("登录失效请重新登录~~");
        }
    }
}
