package com.kkuil.blackchat.core.chat.domain.vo.response.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kkuil.blackchat.core.websocket.domain.enums.ChatActiveStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Kkuil
 * @Date 2023/9/27
 * @Description 群成员列表的成员信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatMemberResp {
    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 用户名
     */
    private String name;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 角色
     */
    private Integer roleId;

    /**
     * @see ChatActiveStatusEnum
     * 当前状态
     */
    private Integer activeStatus;
}
