package com.kkuil.blackchat.web.websocket.utils;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * @Author Kkuil
 * @Date 2023/9/21 12:36
 * @Description netty工具类
 */
public class NettyUtil {

    /**
     * 从通道中获取属性值
     *
     * @param channel 通道
     * @param key     属性名
     * @param <T>     属性值类型
     * @return 属性值
     */
    public static <T> T getAttrFromChannel(Channel channel, AttributeKey<T> key) {
        Attribute<T> attr = channel.attr(key);
        return attr.get();
    }

    /**
     * 向websocket上下文对象中设置属性
     * 类似于向HttpRequest对象中设置属性
     *
     * @param channel 当前频道
     * @param key     设置的Key
     * @param value   设置值
     */
    public static <T> void setAttrInChannel(Channel channel, AttributeKey<T> key, T value) {
        Attribute<T> attr = channel.attr(key);
        attr.set(value);
    }

}
