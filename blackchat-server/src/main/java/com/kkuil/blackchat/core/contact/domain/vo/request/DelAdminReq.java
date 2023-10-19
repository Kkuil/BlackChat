package com.kkuil.blackchat.core.contact.domain.vo.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/10/15 14:57
 * @Description 删除管理的请求信息
 */
@Data
public class DelAdminReq {
    /**
     * 群ID
     */
    @NotNull(message = "群ID不能为空")
    private Long groupId;

    /**
     * 删除管理的列表
     */
    @NotEmpty(message = "管理员列表不能为空")
    private List<Long> uidList;
}
