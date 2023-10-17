package com.kkuil.blackchat.domain.enums;

import com.kkuil.blackchat.domain.enums.error.ErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description 业务校验异常码
 */
@AllArgsConstructor
@Getter
public enum ChatMessageEnum {
    /**
     *
     */
    EXIT_GROUP_SUCCESS(1001, "退出群聊成功"),
    DEL_GROUP_SUCCESS(1002, "删除群聊成功"),
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
