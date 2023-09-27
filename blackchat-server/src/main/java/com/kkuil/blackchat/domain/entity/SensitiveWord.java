package com.kkuil.blackchat.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 敏感词库
 * @TableName sensitive_word
 */
@TableName(value ="sensitive_word")
@Data
public class SensitiveWord implements Serializable {
    /**
     * 敏感词
     */
    @TableField(value = "word")
    private String word;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
