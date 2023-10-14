package com.kkuil.blackchat.core.contact.domain.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Kkuil
 * @Date 2023/10/9 21:43
 * @Description 获取朋友列表响应实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendResp {
    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 房间ID
     */
    private Long roomId;

    /**
     * 用户地区
     */
    private String place;

}
