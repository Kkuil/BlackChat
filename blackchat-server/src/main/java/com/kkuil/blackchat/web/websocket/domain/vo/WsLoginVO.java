package com.kkuil.blackchat.web.websocket.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author Kkuil
 * @Date 2023/9/25
 * @Description 微信登录VO
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class WsLoginVO {
    private String url;
}
