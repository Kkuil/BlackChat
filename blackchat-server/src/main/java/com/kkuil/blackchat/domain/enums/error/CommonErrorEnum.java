package com.kkuil.blackchat.domain.enums.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description 通用异常码
 */
@AllArgsConstructor
@Getter
public enum CommonErrorEnum implements ErrorEnum {
    /**
     *
     */
    SYSTEM_ERROR(-1, "系统出小差了，请稍后再试哦~~"),
    PARAM_VALID(-2, "参数校验失败{0}"),
    FREQUENCY_LIMIT(-3, "请求太频繁了，请稍后再试哦~~"),
    LOCK_LIMIT(-4, "请求太频繁了，请稍后再试哦~~"),
    ;
    private final Integer code;
    private final String msg;

    @Override
    public Integer getErrorCode() {
        return this.code;
    }

    @Override
    public String getErrorMsg() {
        return this.msg;
    }
}
