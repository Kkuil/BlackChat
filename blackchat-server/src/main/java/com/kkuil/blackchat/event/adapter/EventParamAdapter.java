package com.kkuil.blackchat.event.adapter;

import com.kkuil.blackchat.event.domain.dto.UserOnlineEventParamsDTO;
import com.kkuil.blackchat.web.websocket.domain.vo.response.WsBaseResp;
import com.kkuil.blackchat.web.websocket.domain.vo.response.WsLoginSuccessMessage;
import io.netty.channel.Channel;

/**
 * @Author Kkuil
 * @Date 2023/9/27 10:02
 * @Description 事件参数适配器
 */
public class EventParamAdapter {

    /**
     * 构建用户上线事件参数
     *
     * @param channel                  当前通道
     * @param wsLoginSuccessWsBaseResp 用户登录参数
     * @return 参数
     */
    public static UserOnlineEventParamsDTO buildUserOnlineEventParams(
            Channel channel,
            WsBaseResp<WsLoginSuccessMessage> wsLoginSuccessWsBaseResp
    ) {
        UserOnlineEventParamsDTO userOnlineEventParamsDTO = new UserOnlineEventParamsDTO();
        userOnlineEventParamsDTO
                .setChannel(channel)
                .setWsLoginSuccessWsBaseResp(wsLoginSuccessWsBaseResp);
        return userOnlineEventParamsDTO;
    }

}
