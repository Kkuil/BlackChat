package com.kkuil.blackchat.core.websocket.domain.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description 登录成功返回的消息对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WsLoginSuccessMessage {
    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 用户头像信息
     */
    private String avatar;

    /**
     * token
     */
    private String token;

    /**
     * 用户名
     */
    private String name;

    /**
     * 用户权限 9001普通用户 9002超管
     */
    private Long power;
}
