package com.kkuil.blackchat.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户联系人表
 * @TableName user_friend
 */
@TableName(value ="user_friend")
@Data
public class UserFriend implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * uid
     */
    @TableField(value = "uid")
    private Long uid;

    /**
     * 好友uid
     */
    @TableField(value = "friend_uid")
    private Long friend_uid;

    /**
     * 逻辑删除(0-正常,1-删除)
     */
    @TableField(value = "delete_status")
    private Integer delete_status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date create_time;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date update_time;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
