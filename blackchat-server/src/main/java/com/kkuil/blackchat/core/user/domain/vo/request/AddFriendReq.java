package com.kkuil.blackchat.core.user.domain.vo.request;

import lombok.Data;

/**
 * @Author Kkuil
 * @Date 2023/10/11 20:55
 * @Description 加好友申请请求参数
 */
@Data
public class AddFriendReq {

    /**
     * 被申请人的ID
     */
    private Long repliedId;

    /**
     * 申请备注
     */
    private String msg;

}
