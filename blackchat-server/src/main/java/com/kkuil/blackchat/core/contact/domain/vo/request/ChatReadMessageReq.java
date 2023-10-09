package com.kkuil.blackchat.core.contact.domain.vo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author Kkuil
 * @Date 2023/10/3 12:30
 * @Description 消息列表请求
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatReadMessageReq {

    /**
     * 房间ID
     */
    @NotNull
    private Long roomId;
}
