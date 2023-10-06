package com.kkuil.blackchat.core.chat.domain.vo.message.handlers.factory;

import com.kkuil.blackchat.domain.enums.error.CommonErrorEnum;
import com.kkuil.blackchat.utils.AssertUtil;
import com.kkuil.blackchat.core.chat.domain.vo.message.handlers.AbstractMessageHandler;

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
    private static final Map<Integer, AbstractMessageHandler<Object>> STRATEGY_MAP = new HashMap<>();

    /**
     * 注册消息处理器
     *
     * @param code     注册码
     * @param strategy 消息处理器策略
     */
    public static void register(Integer code, AbstractMessageHandler<Object> strategy) {
        STRATEGY_MAP.put(code, strategy);
    }

    /**
     * 通过策略码获取相应消息策略
     *
     * @param type 策略类型
     * @return 策略
     */
    public static AbstractMessageHandler<Object> getStrategyNoNull(Integer type) {
        AbstractMessageHandler<Object> strategy = STRATEGY_MAP.get(type);
        AssertUtil.isNotEmpty(strategy, CommonErrorEnum.PARAM_VALID);
        return strategy;
    }
}
