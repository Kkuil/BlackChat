package com.kkuil.blackchat.utils;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description spring el表达式解析工具类
 */
public class SpElUtil {
    /**
     * EL表达式解析器
     */
    private static final ExpressionParser EL_PARSER = new SpelExpressionParser();

    /**
     * 获取参数名工具
     */
    private static final DefaultParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    /**
     * 解析EL表达式
     *
     * @param method 方法对象
     * @param args   参数值
     * @param spEl   el表达式
     * @return {@link String}
     */
    public static String parseSpEl(Method method, Object[] args, String spEl) {
        // 解析参数名
        String[] params = Optional.ofNullable(PARAMETER_NAME_DISCOVERER.getParameterNames(method)).orElse(new String[]{});
        // el解析需要的上下文对象
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < params.length; i++) {
            // 所有参数都作为原材料扔进去
            context.setVariable(params[i], args[i]);
        }
        Expression expression = EL_PARSER.parseExpression(spEl);
        return expression.getValue(context, String.class);
    }

    /**
     * @param method
     * @return {@link String}
     */
    public static String getMethodKey(Method method) {
        return method.getDeclaringClass() + "#" + method.getName();
    }
}
