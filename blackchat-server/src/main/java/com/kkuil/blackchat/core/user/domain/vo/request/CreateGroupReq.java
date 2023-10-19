package com.kkuil.blackchat.core.user.domain.vo.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/10/11 20:55
 * @Description 创建群聊请求参数
 */
@Data
public class CreateGroupReq {

    /**
     * 被申请人的ID
     */
    @NotNull(message = "群成员不能为空")
    private List<Long> uidList;

    /**
     * 群名
     */
    @NotNull(message = "群名不能为空")
    private String groupName;

    /**
     * 群头像
     */
    private String groupAvatar;

    /**
     * 申请备注
     */
    @NotEmpty(message = "备注不能为空")
    private String msg;
}
