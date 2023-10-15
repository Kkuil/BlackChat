package com.kkuil.blackchat.event.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author Kkuil
 * @Date 2023/10/6 9:39
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsgSendMessageDTO implements Serializable {
    /**
     * 消息ID
     */
    private Long msgId;
}
