package com.kkuil.blackchat.core.contact.domain.vo.request;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @Author Kkuil
 * @Date 2023/10/13 11:31
 * @Description 消息操作请求体
 */
@Data
@Accessors(chain = true)
public class MessageOperationReq {

    /**
     * 消息ID
     */
    @NotNull(message = "消息ID不能为空")
    private Long id;

    /**
     * 操作类型 2同意 3拒绝
     */
    @NotNull(message = "操作类型不能为空")
    private Integer status;

}
