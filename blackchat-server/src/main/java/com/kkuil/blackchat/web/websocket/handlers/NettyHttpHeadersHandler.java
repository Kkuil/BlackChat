package com.kkuil.blackchat.web.websocket.handlers;

import cn.hutool.core.net.url.UrlBuilder;
import com.kkuil.blackchat.web.websocket.constant.AuthorizationConst;
import com.kkuil.blackchat.web.websocket.utils.NettyUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.util.Optional;

/**
 * @Author Kkuil
 * @Date 2023/09/17 17:00
 * @Description
 */
public class NettyHttpHeadersHandler extends ChannelInboundHandlerAdapter {

    /**
     * 请求头中真实IP的key
     */
    public static final String X_REAL_IP = "X-Real-IP";

    /**
     * 在请求参数中的token键
     */
    public static final String HTTP_REQUEST_TOKEN_IN_PARAM = "token";

    @Override
    public void channelRead(ChannelHandlerContext context, Object message) {
        Channel channel = context.channel();
        // 过滤Websocket请求（在升级成Websocket请求前进行请求头信息的截取）
        if (message instanceof HttpRequest request) {
            UrlBuilder urlBuilder = UrlBuilder.ofHttp(request.getUri());
            Optional<String> tokenOptional = Optional.of(urlBuilder)
                    .map(UrlBuilder::getQuery)
                    .map(k -> k.get(HTTP_REQUEST_TOKEN_IN_PARAM))
                    .map(CharSequence::toString);
            // 如果token存在，在上下问对象中插入token属性，以便后续使用
            tokenOptional.ifPresent(s -> NettyUtil.setAttrInChannel(channel, AuthorizationConst.TOKEN_KEY_IN_CHANNEL, s));

            // 移除后面拼接的所有参数，不然后面进行解析的时候，会把token参数解析成url，从而导致连接失败
            request.setUri(urlBuilder.getPath().toString());

            // 获取用户ip（注意：这里如果使用了负载均衡服务器（Nginx等），一定要把真实的IP地址设置回请求头中，不然获取到的就是负载均衡服务器的IP地址）
            String ip = request.headers().get(X_REAL_IP);
            if (StringUtils.isBlank(ip)) {
                // 如果为空，则获取当前连接的远端地址
                InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
                ip = address.getAddress().getHostAddress();
            }
            // 保存到上下文属性中
            NettyUtil.setAttrInChannel(channel, AuthorizationConst.IP_KEY_IN_CHANNEL, ip);
            // 处理器只需要用一次，用完就进行移除
            context.pipeline().remove(this);
        }
        context.fireChannelRead(message);
    }
}
