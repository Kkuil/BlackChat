package com.kkuil.blackchat.core.contact.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Kkuil
 * @Date 2023/10/14 13:16
 * @Description 群成员基本信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMemberBaseInfo {

    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 角色ID
     */
    private Integer role;

}
