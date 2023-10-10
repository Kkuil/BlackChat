package com.kkuil.blackchat.domain.enums.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description 用户校验异常码
 */
@AllArgsConstructor
@Getter
public enum UserErrorEnum implements ErrorEnum {
    /**
     *
     */
    NOT_ENOUGH_UPDATE_NAME_COUNT(7001, "暂无更名次数"),
    USERNAME_IS_USED(7002, "用户名已被使用，请重新输入"),
    USER_NOT_EXIST(7003, "用户不存在"),
    NOT_FRIEND(7004, "您还不是对方的好友，无法操作~"),
    ;

    private final Integer code;
    private final String msg;

    @Override
    public Integer getErrorCode() {
        return code;
    }

    @Override
    public String getErrorMsg() {
        return msg;
    }
}
