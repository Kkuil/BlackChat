package com.kkuil.blackchat.domain.bo.ip;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Author Kkuil
 * @Date 2023/10/4 23:50
 * @Description ip结果
 */
@Data
public class IpResult<T> implements Serializable {
    /**
     * 错误码
     */
    private Integer code;
    /**
     * 错误消息
     */
    private String msg;
    /**
     * 返回对象
     */
    private T data;

    public boolean isSuccess() {
        return Objects.nonNull(this.code) && this.code == 0;
    }
}
