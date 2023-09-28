package com.kkuil.blackchat.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * @Author Kkuil
 * @Date 2023/9/28 10:17
 * @Description 消息表
 */
@Data
@Builder
@TableName(value = "message")
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
    private Long roomId;

    /**
     * 消息发送者uid
     */
    @TableField(value = "from_uid")
    private Long fromUid;

    /**
     * 消息内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 回复的消息内容
     */
    @TableField(value = "reply_message_id")
    private Long replyMessageId;

    /**
     * 消息状态 0正常 1删除
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 与回复的消息间隔多少条
     */
    @TableField(value = "gap_count")
    private Integer gapCount;

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
