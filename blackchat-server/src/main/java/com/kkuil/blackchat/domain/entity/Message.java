package com.kkuil.blackchat.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 消息表
 * @TableName message
 */
@TableName(value ="message")
@Data
public class Message implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会话表id
     */
    @TableField(value = "room_id")
    private Long room_id;

    /**
     * 消息发送者uid
     */
    @TableField(value = "from_uid")
    private Long from_uid;

    /**
     * 消息内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 回复的消息内容
     */
    @TableField(value = "reply_msg_id")
    private Long reply_msg_id;

    /**
     * 消息状态 0正常 1删除
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 与回复的消息间隔多少条
     */
    @TableField(value = "gap_count")
    private Integer gap_count;

    /**
     * 消息类型 1正常文本 2.撤回消息
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 扩展信息
     */
    @TableField(value = "extra")
    private Object extra;

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
