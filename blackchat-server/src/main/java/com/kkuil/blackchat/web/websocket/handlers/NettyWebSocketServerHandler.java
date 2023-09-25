package com.kkuil.blackchat.web.websocket.handlers;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.kkuil.blackchat.web.websocket.constant.AuthorizationConst;
import com.kkuil.blackchat.web.websocket.domain.enums.WsRequestTypeEnum;
import com.kkuil.blackchat.web.websocket.domain.vo.request.WSBaseReq;
import com.kkuil.blackchat.web.websocket.service.IWebSocketService;
import com.kkuil.blackchat.web.websocket.utils.NettyUtil;
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

    private IWebSocketService webSocketService;

    /**
     * 用户上线时，初始化Websocket服务
     *
     * @param context 上下文对象
     */
    @Override
    public void channelActive(ChannelHandlerContext context) {
        // 使用spring的getBean方法获取Bean对象
        webSocketService = SpringUtil.getBean(IWebSocketService.class);
        webSocketService.online(context.channel());
    }

    /**
     * 用户下线时，断开连接
     *
     * @param context 上下文对象
     */
    @Override
    public void channelInactive(ChannelHandlerContext context) {
        Channel channel = context.channel();
        userOffline(channel);
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
            // 获取在升级成Websocket协议之前请求头中携带的token值（在这个类（NettyHttpHeadersHandler）中收集的）
            String token = NettyUtil.getAttrFromChannel(context.channel(), AuthorizationConst.TOKEN_KEY_IN_CHANNEL);
            // 非空则进行权限验证
            if (StrUtil.isNotBlank(token)) {
                webSocketService.authorize(context.channel(), token);
            }
        }
        // 读空闲事件
        if (evt instanceof IdleStateEvent event) {
            // 读空闲则触发用户下线事件
            if (event.state() == IdleState.READER_IDLE) {
                log.info("用户长时间未在线已下线");
                userOffline(context.channel());
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
        Channel channel = context.channel();
        // 转文本
        String text = message.text();
        // 转为websocket请求对象(消息包装)
        WSBaseReq wsBaseReq = JSONUtil.toBean(text, WSBaseReq.class);
        // 获取请求类型
        WsRequestTypeEnum type = WsRequestTypeEnum.of(wsBaseReq.getType());
        // 判断请求类型
        switch (type) {
            case AUTHORIZE:
                // 授权请求
                webSocketService.authorize(channel, wsBaseReq.getData());
                break;
            case HEARTBEAT:
                // 心跳包请求
                break;
            case LOGIN:
                // 登录请求
                webSocketService.login(channel);
                break;
            default:
                // 未知请求
                Map<Integer, WsRequestTypeEnum> wsRequestTypeCache = WsRequestTypeEnum.WS_REQUEST_TYPE_CACHE;
                String wsRequestTypeCacheStr = JSONUtil.toJsonStr(wsRequestTypeCache);
                channel.writeAndFlush(new TextWebSocketFrame(wsRequestTypeCacheStr));
                break;
        }
    }

    /**
     * 用户下线
     *
     * @param channel websocket通道
     */
    private void userOffline(Channel channel) {
        // 删除连接
        webSocketService.offline(channel);
        // 连接关闭
        channel.close();
    }
}
