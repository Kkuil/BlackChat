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
 * @Author Kkuil
 * @Date 2023/9/28 15:04
 * @Description 群成员表
 */
@TableName(value ="group_member")
@Data
public class GroupMember implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 群主id
     */
    @TableField(value = "group_id")
    private Long groupId;

    /**
     * 成员uid
     */
    @TableField(value = "uid")
    private Long uid;

    /**
     * 成员角色 1群主 2管理员 3普通成员
     */
    @TableField(value = "role")
    private Integer role;

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
