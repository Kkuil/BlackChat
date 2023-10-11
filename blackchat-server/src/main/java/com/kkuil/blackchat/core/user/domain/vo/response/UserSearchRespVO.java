package com.kkuil.blackchat.core.user.domain.vo.response;

import lombok.Data;

/**
 * @Author Kkuil
 * @Date 2023/10/11 9:16
 * @Description 用户搜索返回值
 */
@Data
public class UserSearchRespVO {

    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 是否是好友
     */
    private Boolean isFriend;
}
