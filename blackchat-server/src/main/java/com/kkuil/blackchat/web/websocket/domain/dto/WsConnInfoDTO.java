package com.kkuil.blackchat.web.websocket.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author Kkuil
 * @Date 2023/9/25 11:13
 * @Description websocket连接的用户信息
 */
@Data
@Accessors(chain = true)
public class WsConnUserInfoDTO {

    /**
     * 用户ID（如果用户登录了，就进行存储）
     */
    private Long uid;

}
