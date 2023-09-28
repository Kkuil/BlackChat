package com.kkuil.blackchat.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 单聊房间表
 * @TableName room_friend
 */
@TableName(value ="room_friend")
@Data
public class RoomFriend implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 房间id
     */
    @TableField(value = "room_id")
    private Long roomId;

    /**
     * uid1（更小的uid）
     */
    @TableField(value = "uid1")
    private Long uid1;

    /**
     * uid2（更大的uid）
     */
    @TableField(value = "uid2")
    private Long uid2;

    /**
     * 房间key由两个uid拼接，先做排序uid1_uid2
     */
    @TableField(value = "room_key")
    private String roomKey;

    /**
     * 房间状态 0正常 1禁用(删好友了禁用)
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
