package com.kkuil.blackchat.config;

import org.springframework.lang.Nullable;

import java.util.concurrent.Executor;

public interface SecureInvokeConfigurer {

    /**
     * 返回一个线程池
     *
     * @return Executor
     */
    @Nullable
    default Executor getSecureInvokeExecutor() {
        return null;
    }

}
