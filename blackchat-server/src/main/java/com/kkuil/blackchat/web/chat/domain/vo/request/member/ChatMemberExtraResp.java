package com.kkuil.blackchat.web.chat.domain.vo.request.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Kkuil
 * @Date 2023/10/2 17:40
 * @Description 成员列表响应体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMemberExtraResp {

    /**
     * 当前响应的用户状态
     */
    private Integer activeStatus;

    /**
     * 群总数
     */
    private Integer totalCount;
}
