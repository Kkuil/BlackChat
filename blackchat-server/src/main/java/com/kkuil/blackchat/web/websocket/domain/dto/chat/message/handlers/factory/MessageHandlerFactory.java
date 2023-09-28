package com.kkuil.blackchat.web.websocket.domain.dto.chat.message.handlers.factory;

import com.kkuil.blackchat.domain.enums.error.CommonErrorEnum;
import com.kkuil.blackchat.utils.AssertUtil;
import com.kkuil.blackchat.web.websocket.domain.dto.chat.message.handlers.AbstractMessageHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Kkuil
 * @Date 2023/9/28 10:04
 * @Description 消息处理器生产工厂
 */
public class MessageHandlerFactory {

    /**
     * code -> handler映射关系
     */
    private static final Map<Integer, AbstractMessageHandler> STRATEGY_MAP = new HashMap<>();

    /**
     * 注册消息处理器
     *
     * @param code     注册码
     * @param strategy 消息处理器策略
     */
    public static void register(Integer code, AbstractMessageHandler strategy) {
        STRATEGY_MAP.put(code, strategy);
    }

    /**
     * 通过策略码获取相应消息策略
     *
     * @param code 策略码
     * @return 策略
     */
    public static AbstractMessageHandler getStrategyNoNull(Integer code) {
        AbstractMessageHandler strategy = STRATEGY_MAP.get(code);
        AssertUtil.isNotEmpty(strategy, CommonErrorEnum.PARAM_VALID);
        return strategy;
    }
}
