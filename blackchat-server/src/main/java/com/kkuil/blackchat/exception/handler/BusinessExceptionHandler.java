package com.kkuil.blackchat.exception.handler;

import com.kkuil.blackchat.exception.NecessaryFieldsIsEmptyException;
import com.kkuil.blackchat.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author kkuil
 * @Date 2023/08/03 23:00
 * @Description 业务异常处理器
 */
@RestControllerAdvice
@Slf4j
public class BusinessExceptionHandler {
    /**
     * 必要字段为空异常
     *
     * @param e NecessaryFieldsIsEmptyException
     * @return ResultUtil<Boolean>
     */
    @ExceptionHandler(value = NecessaryFieldsIsEmptyException.class)
    public ResultUtil<Boolean> necessaryFieldsIsEmptyException(NecessaryFieldsIsEmptyException e) {
        log.info("Business message: {}", e.getMessage());
        return ResultUtil.error(e.getMessage(), false);
    }
}
