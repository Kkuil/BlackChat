package com.kkuil.blackchat.enums;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description 错误枚举接口
 */
public interface ErrorEnum {

    /**
     * 获取错误码
     *
     * @return 错误码
     */
    Integer getErrorCode();

    /**
     * 获取错误信息
     *
     * @return 错误信息
     */
    String getErrorMsg();
}
