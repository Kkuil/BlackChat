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
 * @Date 2023/10/5 18:57
 * @Description 会话列表
 */
@TableName(value ="contact")
@Data
public class Contact implements Serializable {
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
     * 房间id
     */
    @TableField(value = "room_id")
    private Long roomId;

    /**
     * 阅读到的时间
     */
    @TableField(value = "read_time")
    private Date readTime;

    /**
     * 会话内消息最后更新的时间(只有普通会话需要维护，全员会话不需要维护)
     */
    @TableField(value = "active_time")
    private Date activeTime;

    /**
     * 会话最新消息id
     */
    @TableField(value = "last_msg_id")
    private Long lastMsgId;

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
