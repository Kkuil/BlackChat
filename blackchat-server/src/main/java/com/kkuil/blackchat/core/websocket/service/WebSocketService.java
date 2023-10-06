package com.kkuil.blackchat.core.websocket.service;

import com.kkuil.blackchat.domain.entity.User;
import com.kkuil.blackchat.core.websocket.domain.vo.response.WsBaseResp;
import io.netty.channel.Channel;

/**
 * @Author Kkuil
 * @Date 2023/08/05 12:30
 * @Description
 */
public interface WebSocketService {

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
     * @param channel 通道
     * @param uid     用户ID
     */
    void offline(Channel channel, Long uid);

    /**
     * 扫码登录
     *
     * @param channel 连接通道
     */
    void scan(Channel channel);

    /**
     * 订阅成功
     *
     * @param code 登录码
     */
    void subscribeSuccess(Integer code);

    /**
     * 扫码登录成功
     *
     * @param loginCode 登录吗
     * @param user      用户信息
     * @param token     token
     */
    void scanLoginSuccess(Integer loginCode, User user, String token);

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
     * @param wsBaseResp 需要推送的消息
     */
    void sendMsgToAll(WsBaseResp<?> wsBaseResp);

    /**
     * 向全部人推送消息
     *
     * @param wsBaseResp 发送消息体
     * @param skipUid    跳过的用户ID
     */
    void sendMsgToAll(WsBaseResp<?> wsBaseResp, Long skipUid);

    /**
     * 退出登录
     *
     * @param channel 通道
     */
    void logout(Channel channel);
}
