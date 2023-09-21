package com.kkuil.blackchat.exception.handler;

import com.kkuil.blackchat.utils.ResultUtil;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author kkuil
 * @Date 2023/08/03 23:00
 * @Description 异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 系统错误默认消息
     */
    public static final String SYSTEM_ERROR_MESSAGE = "系统出了点小差，请稍后再试哦~~";

    /**
     * 处理所有不可知的异常
     *
     * @param e Exception
     * @return ResponseEntity
     */
    @ExceptionHandler(value = Exception.class)
    public ResultUtil<Boolean> handleException() {
        return ResultUtil.error(SYSTEM_ERROR_MESSAGE, false);
    }
}
