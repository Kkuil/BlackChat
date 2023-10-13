package com.kkuil.blackchat.core.chat.domain.vo.request.message;

import com.kkuil.blackchat.domain.vo.request.CursorPageBaseReq;
import lombok.*;

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
@EqualsAndHashCode(callSuper = true)
public class ChatMessageCursorReq extends CursorPageBaseReq<String> {

    /**
     * 房间ID
     */
    @NotNull
    private Long roomId;
}
