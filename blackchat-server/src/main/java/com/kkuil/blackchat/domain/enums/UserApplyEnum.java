package com.kkuil.blackchat.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description 用户申请类型枚举
 */
@AllArgsConstructor
@Getter
public enum UserApplyEnum {
    /**
     *
     */
    FRIEND(1, "好友"),
    GROUP(2, "群聊"),
    ;
    private final Integer type;
    private final String msg;

    public String getMsg() {
        return msg;
    }

    public Integer getType() {
        return type;
    }
}
