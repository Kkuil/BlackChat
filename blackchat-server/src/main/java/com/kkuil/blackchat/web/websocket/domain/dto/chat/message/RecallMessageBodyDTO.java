package com.kkuil.blackchat.web.websocket.domain.dto.chat.message;

import com.kkuil.blackchat.web.websocket.domain.dto.chat.AbstractChatMessageBaseReq;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author Kkuil
 * @Date 2023/9/28
 * @Description 消息撤回
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RecallMessageBodyDTO extends AbstractChatMessageBaseReq implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 撤回消息的uid
     */
    private Long recallUid;

    /**
     * 撤回的时间点
     */
    private Date recallTime;
}
