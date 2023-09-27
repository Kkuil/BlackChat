package com.kkuil.blackchat.event.listener;

import com.kkuil.blackchat.event.UserOnlineEvent;
import com.kkuil.blackchat.event.domain.dto.UserOnlineEventParamsDTO;
import com.kkuil.blackchat.web.websocket.domain.vo.response.WsBaseResp;
import com.kkuil.blackchat.web.websocket.domain.vo.response.WsLoginSuccessMessage;
import com.kkuil.blackchat.web.websocket.service.WebSocketService;
import io.netty.channel.Channel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Author Kkuil
 * @Date 2023/9/27 9:44
 * @Description 用户上线事件监听器
 */
@Component
@Slf4j
public class UserOnlineEventListeners {

    @Resource
    private WebSocketService webSocketService;

    /**
     * 发送给用户登录成功
     *
     * @param event 用户上线事件参数
     */
    @Async
    @EventListener(classes = UserOnlineEvent.class)
    public void sendMsgToOne(UserOnlineEvent event) {
        UserOnlineEventParamsDTO userOnlineEventParamsDTO = event.getUserOnlineEventParamsDTO();
        Channel channel = userOnlineEventParamsDTO.getChannel();
        WsBaseResp<WsLoginSuccessMessage> wsLoginSuccessWsBaseResp = userOnlineEventParamsDTO.getWsLoginSuccessWsBaseResp();
        webSocketService.sendMsgToOne(channel, wsLoginSuccessWsBaseResp);
    }

    /**
     * 发送给所有用户更新上线列表
     *
     * @param userOnlineEventParamsDTO 用户上线事件参数
     */
    @Async
    @EventListener(classes = UserOnlineEvent.class)
    public void updateOnlineList(UserOnlineEvent event) {
    }
}
