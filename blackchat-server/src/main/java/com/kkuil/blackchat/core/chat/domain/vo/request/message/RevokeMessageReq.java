package com.kkuil.blackchat.core.chat.domain.vo.request.message;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author Kkuil
 * @Date 2023/10/13 18:58
 * @Description 撤回消息请求体
 */
@Data
public class RevokeMessageReq {

    /**
     * 消息ID
     */
    @NotNull(message = "消息ID不能为空")
    private Long id;

}
