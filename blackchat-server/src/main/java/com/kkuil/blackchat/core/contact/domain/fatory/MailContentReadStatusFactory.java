package com.kkuil.blackchat.core.contact.domain.fatory;

import com.kkuil.blackchat.core.contact.domain.fatory.handlers.AbstractReadStatusContentHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Kkuil
 * @Date 2023/10/13 16:06
 * @Description 邮箱消息内容工厂
 */
public class MailContentReadStatusFactory {

    public static final Map<Integer, AbstractReadStatusContentHandler> STATUS_FACTORY = new HashMap<>(8);

    /**
     * 注册处理器
     *
     * @param status  状态
     * @param handler 处理器
     */
    public static void register(Integer status, AbstractReadStatusContentHandler handler) {
        STATUS_FACTORY.put(status, handler);
    }

    /**
     * 获取处理器
     *
     * @param status 状态
     * @return 处理器
     */
    public static AbstractReadStatusContentHandler getHandler(int status) {
        return STATUS_FACTORY.get(status);
    }

}
