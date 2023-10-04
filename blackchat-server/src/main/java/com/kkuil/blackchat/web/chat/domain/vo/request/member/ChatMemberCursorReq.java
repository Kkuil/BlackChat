package com.kkuil.blackchat.web.chat.domain.vo.request.member;

import com.kkuil.blackchat.domain.vo.request.CursorPageBaseReq;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * @Author Kkuil
 * @Date 2023/10/1 13:11
 * @Description 获取成员列表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChatMemberCursorReq extends CursorPageBaseReq {

    /**
     * 房间号
     */
    @NotNull
    private Long roomId = 1L;

    /**
     * 在线状态
     */
    @NotNull
    private Integer activeStatus;
}
