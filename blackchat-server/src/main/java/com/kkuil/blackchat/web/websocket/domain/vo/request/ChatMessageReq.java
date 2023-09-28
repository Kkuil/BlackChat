package com.kkuil.blackchat.web.websocket.domain.vo.request;

import com.kkuil.blackchat.web.websocket.domain.dto.chat.AbstractChatMessageBaseReq;
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
public class ChatMessageReq<DT extends AbstractChatMessageBaseReq> {
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
     * 消息体
     */
    @NotNull
    private DT body;
}
