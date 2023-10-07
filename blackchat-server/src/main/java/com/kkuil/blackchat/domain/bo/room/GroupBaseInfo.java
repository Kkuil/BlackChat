package com.kkuil.blackchat.domain.bo.room;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author Kkuil
 * @Date 2023/10/5 19:07
 * @Description 会话基本信息
 */
@Data
public class GroupBaseInfo {
    /**
     * 房间id
     */
    private Long roomId;

    /**
     * 群名称
     */
    private String name;

    /**
     * 群头像
     */
    private String avatar;

    /**
     * 额外信息（根据不同类型房间有不同存储的东西）
     */
    private Object extJson;

    /**
     * 逻辑删除(0-正常,1-删除)
     */
    private Integer deleteStatus;
}
