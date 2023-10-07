package com.kkuil.blackchat.domain.bo.contact;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author Kkuil
 * @Date 2023/10/5 18:57
 * @Description 会话列表
 */
@Data
public class ContactWithActiveMsg implements Serializable {
    /**
     * uid
     */
    private Long uid;

    /**
     * 房间id
     */
    private Long roomId;

    /**
     * 阅读到的时间
     */
    private Date readTime;

    /**
     * 会话内消息最后更新的时间(只有普通会话需要维护，全员会话不需要维护)
     */
    private Date activeTime;

    /**
     * 会话最新消息id
     */
    private Long lastMsgId;
}
