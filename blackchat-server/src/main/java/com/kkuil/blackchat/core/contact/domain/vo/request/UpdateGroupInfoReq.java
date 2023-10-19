package com.kkuil.blackchat.core.contact.domain.vo.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author Kkuil
 * @Date 2023/10/15 14:57
 * @Description 添加管理的请求信息
 */
@Data
public class UpdateGroupInfoReq {
    /**
     * 群ID
     */
    @NotNull(message = "群ID不能为空")
    private Long groupId;

    /**
     * 群名称
     */
    @NotNull(message = "群名称不能为空")
    private String groupName;

    /**
     * 群头像
     */
    private String groupAvatar;
}
