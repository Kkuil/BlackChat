package com.kkuil.blackchat.core.websocket.domain.vo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description 消息发送请求体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageReq {
    /**
     * 房间号
     */
    @NotNull
    private Long roomId;

    /**
     * 消息类型
     */
    @NotNull
    private Integer messageType;

    /**
     * 回复的消息id,如果没有别传就好
     */
    private Long replyMessageId;

    /**
     * 消息体
     */
    @NotNull
    private Object body;
}
