package com.kkuil.blackchat.core.user.domain.vo.request;

import lombok.Data;

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
    private List<Long> uidList;

    /**
     * 申请备注
     */
    private String msg;
}
