package com.kkuil.blackchat.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description 阅读状态枚举
 */
@AllArgsConstructor
@Getter
public enum ReadStatusEnum {
    /**
     *
     */
    UNREAD(1, "未读"),
    READ(2, "已读"),
    ;
    private final Integer status;
    private final String msg;

    public Integer getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
