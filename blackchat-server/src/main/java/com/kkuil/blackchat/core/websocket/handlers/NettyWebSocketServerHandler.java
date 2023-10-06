package com.kkuil.blackchat.core.websocket.handlers;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.kkuil.blackchat.core.websocket.constant.AuthorizationConst;
import com.kkuil.blackchat.core.websocket.domain.enums.WsRequestTypeEnum;
import com.kkuil.blackchat.core.websocket.domain.vo.request.WsBaseReq;
import com.kkuil.blackchat.core.websocket.service.WebSocketService;
import com.kkuil.blackchat.core.websocket.utils.NettyUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Author Kkuil
 * @Date 2023/09/17 17:00
 * @Description Websocket 处理器
 */
@Slf4j
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private WebSocketService webSocketService;

    /**
     * 用户连接时，初始化Websocket服务
     *
     * @param context 上下文对象
     */
    @Override
    public void handlerAdded(ChannelHandlerContext context) {
        this.webSocketService = SpringUtil.getBean(WebSocketService.class);
    }

    /**
     * 客户端离线
     *
     * @param context 上下文对象
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext context) {
        Channel channel = context.channel();
        disconnect(channel);
    }

    /**
     * 用户下线时，断开连接
     *
     * @param context 上下文对象
     */
    @Override
    public void channelInactive(ChannelHandlerContext context) {
        Channel channel = context.channel();
        disconnect(channel);
    }


    /**
     * 用户触发的行为处理器
     *
     * @param context 上下文对象
     * @param evt     触发的事件
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext context, Object evt) {
        // 握手认证事件
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            // 保存连接
            webSocketService.connect(context.channel());
            // 获取在升级成Websocket协议之前请求头中携带的token值（在这个类（NettyHttpHeadersHandler）中收集的）
            String token = NettyUtil.getAttrFromChannel(context.channel(), AuthorizationConst.TOKEN_KEY_IN_CHANNEL);
            if (StrUtil.isNotBlank(token)) {
                // 非空则进行权限验证
                webSocketService.authorize(context.channel(), token);
            }
        }
        // 读空闲事件
        if (evt instanceof IdleStateEvent event) {
            // 读空闲则触发用户下线事件
            if (event.state() == IdleState.READER_IDLE) {
                log.info("长时间无操作，强制断开连接");
                disconnect(context.channel());
            }
        }
    }

    /**
     * 异常捕获
     *
     * @param context 上线对象
     * @param cause   异常原因
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
        log.error("异常捕获", cause);
        super.exceptionCaught(context, cause);
    }

    /**
     * 消息读取事件
     *
     * @param context 上线文对象
     * @param message 客户端发送的消息
     */
    @Override
    protected void channelRead0(ChannelHandlerContext context, TextWebSocketFrame message) {
        log.info("message: {}", message.text());
        Channel channel = context.channel();
        // 转文本
        String text = message.text();
        // 转为websocket请求对象(消息包装)
        WsBaseReq wsBaseReq = JSONUtil.toBean(text, WsBaseReq.class);
        // 获取请求类型
        WsRequestTypeEnum type = WsRequestTypeEnum.of(wsBaseReq.getType());
        // 判断请求类型
        switch (type) {
            case AUTHORIZE ->
                // 授权请求
                    webSocketService.authorize(channel, wsBaseReq.getData());
            case HEARTBEAT ->
                // 心跳包请求
                    log.info("type: {}", type);
            case LOGIN ->
                // 登录请求
                    webSocketService.scan(channel);
            case LOGOUT ->
                // 登录请求
                    webSocketService.logout(channel);
            default -> {
                // 未知请求
                Map<Integer, WsRequestTypeEnum> wsRequestTypeCache = WsRequestTypeEnum.WS_REQUEST_TYPE_CACHE;
                String wsRequestTypeCacheStr = JSONUtil.toJsonStr(wsRequestTypeCache);
                channel.writeAndFlush(new TextWebSocketFrame(wsRequestTypeCacheStr));
            }
        }
    }

    /**
     * 用户下线
     *
     * @param channel websocket通道
     */
    private void disconnect(Channel channel) {
        // 断开连接
        webSocketService.disconnect(channel);
        // 连接关闭
        channel.close();
    }
}
