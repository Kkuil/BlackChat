package com.kkuil.blackchat.config;

import com.kkuil.blackchat.utils.sensitive.SensitiveWordBs;
import com.kkuil.blackchat.utils.sensitive.impl.DFAFilter;
import com.kkuil.blackchat.utils.sensitive.impl.MyWordDeny;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Kkuil
 * @Date 2023/9/28 17:14
 * @Description 敏感词配置类
 */
@Configuration
public class SensitiveWordConfig {

    @Resource
    private MyWordDeny myWordDeny;

    /**
     * 初始化引导类
     *
     * @return 初始化引导类
     * @since 1.0.0
     */
    @Bean
    public SensitiveWordBs sensitiveWordBs() {
        return SensitiveWordBs.newInstance()
                .filterStrategy(DFAFilter.getInstance())
                .sensitiveWord(myWordDeny)
                .init();
    }

}
