package com.kkuil.blackchat.config;

import com.kkuil.blackchat.interceptors.CollectorInterceptor;
import com.kkuil.blackchat.interceptors.LogInterceptor;
import com.kkuil.blackchat.interceptors.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author Kkuil
 * @Description 全局拦截器配置
 * @Date 2023/09/03
 */
@Order(-1)
@Configuration
public class GlobalInterceptorConfig implements WebMvcConfigurer {

    /**
     * 拦截器
     *
     * @param interceptorRegistry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        interceptorRegistry
                .addInterceptor(new LogInterceptor())
                .addPathPatterns("/**");
        interceptorRegistry
                .addInterceptor(new TokenInterceptor())
                .excludePathPatterns("/public/**")
                .excludePathPatterns("/wx/**");
        interceptorRegistry
                .addInterceptor(new CollectorInterceptor())
                .addPathPatterns("/**");
    }

    /**
     * 跨域
     *
     * @param corsRegistry 跨域注册器
     */
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        // 覆盖所有请求
        corsRegistry.addMapping("/**")
                // 允许发送 Cookie
                .allowCredentials(true)
                // 放行哪些域名（必须用 patterns，否则 * 会和 allowCredentials 冲突）
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("*");


    }
}
