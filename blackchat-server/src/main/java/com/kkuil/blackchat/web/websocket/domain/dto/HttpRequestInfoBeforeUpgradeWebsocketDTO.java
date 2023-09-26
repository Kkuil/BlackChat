package com.kkuil.blackchat.web.websocket.domain.dto;

import lombok.Builder;
import lombok.Data;


/**
 * @Author Kkuil
 * @Date 2023/9/21 13:13
 * @Description 升级到Websocket协议之前的Http请求信息
 */
@Data
@Builder
public class HttpRequestInfoBeforeUpgradeWebsocketDTO {
    /**
     * 存于上下文对象中的token key
     */
    String token;

    /**
     * 存于上下文对象中的ip key
     */
    String ip;

    /**
     * 存于上下文对象中的ip key
     */
    Long uid;
}
