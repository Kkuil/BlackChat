package com.kkuil.blackchat.event.listener;

import com.kkuil.blackchat.dao.UserDAO;
import com.kkuil.blackchat.domain.entity.User;
import com.kkuil.blackchat.event.UserOnlineEvent;
import com.kkuil.blackchat.event.domain.dto.UserOnlineEventParamsDTO;
import com.kkuil.blackchat.service.IpService;
import com.kkuil.blackchat.web.websocket.constant.AuthorizationConst;
import com.kkuil.blackchat.web.websocket.domain.enums.ChatActiveStatusEnum;
import com.kkuil.blackchat.web.websocket.domain.vo.response.WsBaseResp;
import com.kkuil.blackchat.web.websocket.domain.vo.response.WsLoginSuccessMessage;
import com.kkuil.blackchat.web.websocket.service.WebSocketService;
import com.kkuil.blackchat.web.websocket.utils.NettyUtil;
import io.netty.channel.Channel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

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

    @Resource
    private UserDAO userDao;

    @Resource
    private IpService ipService;

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
     * 更新数据库
     *
     * @param event 用户上线事件参数
     */
    @Async
    @EventListener(classes = UserOnlineEvent.class)
    public void saveDB(UserOnlineEvent event) {
        UserOnlineEventParamsDTO userOnlineEventParamsDTO = event.getUserOnlineEventParamsDTO();
        Long uid = userOnlineEventParamsDTO.getWsLoginSuccessWsBaseResp().getData().getUid();
        User user = userDao.getById(uid);
        User update = new User();
        update.setId(user.getId());
        update.setLastOptTime(user.getLastOptTime());
        user.refreshIp(NettyUtil.getAttrFromChannel(userOnlineEventParamsDTO.getChannel(), AuthorizationConst.IP_KEY_IN_CHANNEL));
        update.setIpInfo(user.getIpInfo());
        update.setActiveStatus(ChatActiveStatusEnum.ONLINE.getStatus());
        userDao.updateById(update);
        // 更新用户ip详情
        ipService.refreshIpDetailAsync(user.getId());
    }
}
