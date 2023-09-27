package com.kkuil.blackchat.web.websocket.domain.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author Kkuil
 * @Date 2023/9/27
 * @Description 群成员列表的成员信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMemberResp {

    /**
     * 用户ID
     */
    private Long uid;

    /**
     * @see com.kkuil.blackchat.web.websocket.domain.enums.ChatActiveStatusEnum
     * 当前状态
     */
    private Integer activeStatus;

    /**
     * 最后一次上下线时间
     */
    private Date lastOptTime;
}
