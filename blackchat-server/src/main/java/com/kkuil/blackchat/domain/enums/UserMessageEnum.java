package com.kkuil.blackchat.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description 业务校验异常码
 */
@AllArgsConstructor
@Getter
public enum UserMessageEnum {
    /**
     *
     */
    DEL_FRIEND_SUCCESS(1001, "删除好友成功"),
    ;
    private final Integer code;
    private final String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
