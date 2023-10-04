package com.kkuil.blackchat.web.chat.domain.vo.request.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author Kkuil
 * @Date 2023/9/28
 * @Description 消息基础请求体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageBaseReq {
    /**
     * 消息ID（由前端生成）
     */
    @NotNull
    private Long messageId;

    /**
     * 房间ID
     */
    @NotNull
    private Long roomId;
}
