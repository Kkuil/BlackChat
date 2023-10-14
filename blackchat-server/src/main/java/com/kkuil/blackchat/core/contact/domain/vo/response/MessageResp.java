package com.kkuil.blackchat.core.contact.domain.vo.response;

import lombok.Data;

/**
 * @Author Kkuil
 * @Date 2023/10/12 22:51
 * @Description 获取消息响应类
 */
@Data
public class MessageResp {
    /**
     * 消息ID
     */
    private Long id;

    /**
     * 消息的发送者ID  TODO: 在这里不传用户名和头像是因为前端有缓存则走缓存，减少带宽
     */
    private Long uid;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 备注
     */
    private String msg;

    /**
     * 消息类型 1未读 2已读
     */
    private Integer type;

}
