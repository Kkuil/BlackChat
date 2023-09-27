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
 * @Date 2023/9/27
 * @Description 功能物品配置表
 */
@TableName(value = "item_config")
@Data
public class ItemConfig implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 物品类型 1改名卡 2徽章
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 物品图片
     */
    @TableField(value = "img")
    private String img;

    /**
     * 物品功能描述
     */
    @TableField(value = "`describe`")
    private String describe;

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
