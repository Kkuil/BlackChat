package com.kkuil.blackchat.domain.bo;

import lombok.Data;

/**
 * @Author Kkuil
 * @Date 2023/10/5 19:07
 * @Description 会话基本信息
 */
@Data
public class FriendBaseInfo {
    /**
     * 房间id
     */
    private Long roomId;

    /**
     * uid1（更小的uid）
     */
    private Long uid1;

    /**
     * uid2（更大的uid）
     */
    private Long uid2;

    /**
     * 房间key由两个uid拼接，先做排序uid1_uid2
     */
    private String roomKey;

    /**
     * 房间状态 0正常 1禁用(删好友了禁用)
     */
    private Integer status;
}
