package com.kkuil.blackchat.anotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author Kkuil
 * @Date 2023/9/3 16:30
 * @Description 管理员验证注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@AuthLogin
public @interface AuthAdmin {
}
