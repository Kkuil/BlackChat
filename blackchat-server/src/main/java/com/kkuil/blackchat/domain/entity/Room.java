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
 * @Date 2023/9/28 14:58
 * @Description 房间表
 */
@TableName(value = "room")
@Data
public class Room implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 房间类型 1群聊 2单聊
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 是否全员展示 0否 1是
     */
    @TableField(value = "hot_flag")
    private Integer hotFlag;

    /**
     * 群最后消息的更新时间（热点群不需要写扩散，只更新这里）
     */
    @TableField(value = "active_time")
    private Date activeTime;

    /**
     * 会话中的最后一条消息id
     */
    @TableField(value = "last_msg_id")
    private Long lastMsgId;

    /**
     * 额外信息（根据不同类型房间有不同存储的东西）
     */
    @TableField(value = "ext_json")
    private Object extJson;

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
