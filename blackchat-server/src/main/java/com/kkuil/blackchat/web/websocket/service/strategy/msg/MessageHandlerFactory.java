package com.kkuil.blackchat.web.websocket.service.strategy.msg;


import com.kkuil.blackchat.enums.CommonErrorEnum;
import com.kkuil.blackchat.utils.AssertUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description 消息处理工厂类
 */
public class MessageHandlerFactory {
    private static final Map<Integer, AbstractMessageHandler> STRATEGY_MAP = new HashMap<>();

    public static void register(Integer code, AbstractMessageHandler strategy) {
        STRATEGY_MAP.put(code, strategy);
    }

    public static AbstractMessageHandler getStrategyNoNull(Integer code) {
        AbstractMessageHandler strategy = STRATEGY_MAP.get(code);
        AssertUtil.isNotEmpty(strategy, CommonErrorEnum.PARAM_VALID);
        return strategy;
    }
}
