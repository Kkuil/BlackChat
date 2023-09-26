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
     * 连接
     *
     * @param channel 连接通道
     */
    void connect(Channel channel);

    /**
     * 断开连接
     *
     * @param channel 连接通道
     */
    void disconnect(Channel channel);

    /**
     * 用户在线
     *
     * @param channel 连接通道
     * @param uid     用户ID
     */
    void online(Channel channel, Long uid);

    /**
     * 下线
     *
     * @param channel 断开连接
     */
    void offline(Channel channel);

    /**
     * 处理登录请求
     *
     * @param channel 连接通道
     */
    void login(Channel channel);

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
     * 向某个人发送消息
     *
     * @param channel    通道
     * @param wsBaseResp 消息体
     */
    void sendMsgToOne(Channel channel, WsBaseResp<?> wsBaseResp);

    /**
     * 向全部人推送消息
     *
     * @param msg 需要推送的消息
     */
    void sendMsgToAll(WsBaseResp<?> msg);
}
