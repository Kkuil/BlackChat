package com.kkuil.blackchat.web.chat.domain.vo.request;

import com.kkuil.blackchat.domain.vo.request.CursorPageBaseReq;
import lombok.*;

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
public class MemberReq extends CursorPageBaseReq {

    /**
     * 房间号
     */
    private Long roomId = 1L;
}
