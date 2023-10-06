package com.kkuil.blackchat.core.websocket.handlers;

import com.kkuil.blackchat.domain.dto.RequestHolderDTO;
import com.kkuil.blackchat.domain.dto.RequestInfo;
import com.kkuil.blackchat.core.websocket.constant.AuthorizationConst;
import com.kkuil.blackchat.core.websocket.utils.NettyUtil;
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
        String ip = NettyUtil.getAttrFromChannel(ctx.channel(), AuthorizationConst.IP_KEY_IN_CHANNEL);
        RequestInfo info = new RequestInfo();
        info.setUid(uid);
        info.setIp(ip);
        RequestHolderDTO.set(info);
        // 继续向下传递消息
        ctx.fireChannelRead(message);
    }
}
