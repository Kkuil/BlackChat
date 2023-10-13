package com.kkuil.blackchat.core.contact.domain.vo.request;

import lombok.Data;

/**
 * @Author Kkuil
 * @Date 2023/10/12 22:51
 * @Description 获取消息请求类
 */
@Data
public class MessageReq {

    /**
     * 消息类型 1未读 2已读
     */
    private Integer status;

}
