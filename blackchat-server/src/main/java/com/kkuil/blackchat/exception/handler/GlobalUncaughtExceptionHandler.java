package com.kkuil.blackchat.exception.handler;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description 全局未捕获异常
 */
@Slf4j
public class GlobalUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {


    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("Exception in thread {} ", t.getName(), e);
    }

}
