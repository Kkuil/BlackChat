package com.kkuil.blackchat.core.contact.domain.vo.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/10/15 14:57
 * @Description 邀请用户的请求信息
 */
@Data
public class InvitAddGroupReq {
    /**
     * 群ID
     */
    @NotNull(message = "群ID不能为空")
    private Long groupId;

    /**
     * 备注
     */
    @NotNull(message = "备注不能为空")
    private String msg;

    /**
     * 邀请用户的列表
     */
    @NotNull(message = "邀请成员不能为空")
    private List<Long> uidList;
}
