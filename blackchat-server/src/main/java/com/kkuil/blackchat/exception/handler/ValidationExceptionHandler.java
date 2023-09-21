package com.kkuil.blackchat.exception.handler;

import com.kkuil.blackchat.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author Kkuil
 * @Date 2023/9/16 22:56
 * @Description 入参校验异常捕获器
 */
@RestControllerAdvice
@Slf4j
public class ValidationExceptionHandler {

    /**
     * 字段验证异常
     *
     * @param e 异常
     * @return 异常体信息
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResultUtil<?> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder message = new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach(error -> message.append(error.getField()));
        return ResultUtil.error(message);
    }
}
