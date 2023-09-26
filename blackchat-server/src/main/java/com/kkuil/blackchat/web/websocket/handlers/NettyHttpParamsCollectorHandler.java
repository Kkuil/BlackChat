package com.kkuil.blackchat.web.websocket.handlers;

import com.kkuil.blackchat.web.websocket.constant.AuthorizationConst;
import com.kkuil.blackchat.web.websocket.domain.dto.HttpRequestInfoBeforeUpgradeWebsocket;
import com.kkuil.blackchat.web.websocket.utils.NettyUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author Kkuil
 * @Date 2023/09/17 20:30:00
 * @Description Http请求头属性收集器
 */
@Slf4j
public class NettyHttpParamsCollectorHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object message) {
        Long uid = NettyUtil.getAttrFromChannel(ctx.channel(), AuthorizationConst.UID_KEY_IN_CHANNEL);
        String token = NettyUtil.getAttrFromChannel(ctx.channel(), AuthorizationConst.TOKEN_KEY_IN_CHANNEL);
        String ip = NettyUtil.getAttrFromChannel(ctx.channel(), AuthorizationConst.IP_KEY_IN_CHANNEL);
        HttpRequestInfoBeforeUpgradeWebsocket.builder()
                .uid(uid)
                .token(token)
                .ip(ip);
        // 继续向下传递消息
        ctx.fireChannelRead(message);
    }
}
