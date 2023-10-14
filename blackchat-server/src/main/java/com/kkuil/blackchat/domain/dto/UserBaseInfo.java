package com.kkuil.blackchat.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Kkuil
 * @Date 2023/10/1 19:23
 * @Description 用户基础数据
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBaseInfo {
    /**
     * 用户ID
     */
    private Long id;

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
    private Long roleId;

    /**
     * 用户ip信息
     */
    private IpInfo ipInfo;

    /**
     * 用户状态
     */
    private Integer activeStatus;
}
