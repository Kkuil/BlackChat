package com.kkuil.blackchat.web.websocket.service.impl;

import com.kkuil.blackchat.web.websocket.domain.vo.response.WsBaseResp;
import com.kkuil.blackchat.web.websocket.service.IWebSocketService;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

/**
 * @Author Kkuil
 * @Date 202309/17 17:00:00
 * @Description websocket服务类
 */
@Service
public class IWebSocketServiceImpl implements IWebSocketService {

    /**
     * 用户连接
     *
     * @param channel 连接通道
     */
    @Override
    public void online(Channel channel) {

    }

    /**
     * 处理登录请求
     *
     * @param channel 连接通道
     */
    @Override
    public void login(Channel channel) {

    }

    /**
     * 下线
     *
     * @param channel 断开连接
     */
    @Override
    public void offline(Channel channel) {

    }

    /**
     * 扫码登录
     *
     * @param code 登录码
     * @param uid  用户ID
     */
    @Override
    public void handleScanLogin(Integer code, Long uid) {

    }

    /**
     * 等待授权事件
     *
     * @param code 登录码
     */
    @Override
    public void waitAuthorize(Integer code) {

    }

    /**
     * 授权
     *
     * @param channel 当前通道
     * @param token   用户token
     */
    @Override
    public void authorize(Channel channel, String token) {

    }

    /**
     * 向全部人推送消息
     *
     * @param msg 需要推送的消息
     */
    @Override
    public void sendMsgToAll(WsBaseResp<?> msg) {

    }
}
