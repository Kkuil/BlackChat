package com.kkuil.blackchat.domain.bo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author Kkuil
 * @Date 2023/10/5 19:07
 * @Description 会话基本信息
 */
@Data
public class ContactBaseInfo {
    /**
     * 房间id
     */
    @NotNull
    private Long roomId;

    /**
     * 阅读到的时间
     */
    @NotNull
    private Date readTime;

    /**
     * 会话内消息最后更新的时间(只有普通会话需要维护，全员会话不需要维护)
     */
    @NotNull
    private Date activeTime;

    /**
     * 会话最新消息id
     */
    @NotNull
    private Long lastMsgId;
}
