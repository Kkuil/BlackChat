package com.kkuil.blackchat.web.websocket.service;

import com.kkuil.blackchat.web.websocket.domain.vo.response.WsBaseResp;
import io.netty.channel.Channel;

/**
 * @Author Kkuil
 * @Date 2023/08/05 12:30
 * @Description
 */
public interface IWebSocketService {

    /**
     * 用户连接
     *
     * @param channel 连接通道
     */
    void online(Channel channel);

    /**
     * 处理登录请求
     *
     * @param channel 连接通道
     */
    void login(Channel channel);

    /**
     * 下线
     *
     * @param channel 断开连接
     */
    void offline(Channel channel);

    /**
     * 扫码登录
     *
     * @param code 登录码
     * @param uid  用户ID
     */
    void handleScanLogin(Integer code, Long uid);

    /**
     * 等待授权事件
     *
     * @param code 登录码
     */
    void waitAuthorize(Integer code);

    /**
     * 授权
     *
     * @param channel 当前通道
     * @param token   用户token
     */
    void authorize(Channel channel, String token);

    /**
     * 向全部人推送消息
     *
     * @param msg 需要推送的消息
     */
    void sendMsgToAll(WsBaseResp<?> msg);
}
