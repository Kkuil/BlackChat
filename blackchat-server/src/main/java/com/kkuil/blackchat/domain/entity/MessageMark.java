package com.kkuil.blackchat.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 消息标记表
 * @TableName message_mark
 */
@TableName(value ="message_mark")
@Data
public class MessageMark implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 消息表id
     */
    @TableField(value = "msg_id")
    private Long msg_id;

    /**
     * 标记人uid
     */
    @TableField(value = "uid")
    private Long uid;

    /**
     * 标记类型 1点赞 2举报
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 消息状态 0正常 1取消
     */
    @TableField(value = "status")
    private Integer status;

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
