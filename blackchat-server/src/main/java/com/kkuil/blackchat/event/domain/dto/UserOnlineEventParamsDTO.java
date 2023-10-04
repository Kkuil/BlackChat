package com.kkuil.blackchat.event.domain.dto;

import com.kkuil.blackchat.web.websocket.domain.vo.response.WsBaseResp;
import com.kkuil.blackchat.web.websocket.domain.vo.response.WsLoginSuccessMessage;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @Author Kkuil
 * @Date 2023/9/27 10:08
 * @Description 用户上线事件参数DTO
 */
@Data
@Accessors(chain = true)
@ToString
public class UserOnlineEventParamsDTO {
    /**
     * 通道
     */
    private Channel channel;

    /**
     * 用户登录成功体
     */
    private WsBaseResp<WsLoginSuccessMessage> wsLoginSuccessWsBaseResp;
}
