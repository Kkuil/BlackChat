package com.kkuil.blackchat;

import com.kkuil.blackchat.service.LoginService;
import com.kkuil.blackchat.utils.JwtUtil;
import jakarta.annotation.Resource;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

import static com.kkuil.blackchat.constant.UserConst.USER_TOKEN_SECRET;

/**
 * @Author Kkuil
 * @Date 2023/9/21
 * @Description 初始化测试
 */
// @SpringBootTest
public class InitTests {

    @Resource
    private StringEncryptor stringEncryptor;

    @Resource
    private LoginService loginService;

    /**
     * 获取加密后的mysql账号密码
     */
    @Test
    public void getEncForMysql() {
        String username = "test";
        String password = "123456";
        String encryptUsername = stringEncryptor.encrypt(username);
        String encryptPassword = stringEncryptor.encrypt(password);
        System.out.println("encryptUsername = " + encryptUsername);
        System.out.println("encryptPassword = " + encryptPassword);
    }

    /**
     * 获取加密后的redis密码
     */
    @Test
    public void getEncForRedis() {
        String password = "123456";
        String encryptPassword = stringEncryptor.encrypt(password);
        System.out.println("encryptPassword = " + encryptPassword);
    }

    /**
     * 获取加密后的redis密码
     */
    @Test
    public void getToken() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("uid", 1);
        String token = JwtUtil.create(data, USER_TOKEN_SECRET, 60 * 30 * 60 * 1000);
        System.out.println("token = " + token);
    }
}
