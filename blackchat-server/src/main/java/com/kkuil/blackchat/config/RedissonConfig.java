package com.kkuil.blackchat.config;

import jakarta.annotation.Resource;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description redisson配置
 */
@Configuration
public class RedissonConfig {
    @Resource
    private RedisProperties redisProperties;

//    @Bean
//    public RedissonClient redissonClient() {
//        Config config = new Config();
//        String url = "redis://" + redisProperties.getHost() + ":" + redisProperties.getPort();
//        config.useSingleServer()
//                .setAddress(url)
//                .setPassword(redisProperties.getPassword())
//                .setDatabase(redisProperties.getDatabase());
//        return Redisson.create(config);
//    }
}
