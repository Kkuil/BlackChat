package com.kkuil.blackchat.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kkuil.blackchat.domain.vo.request.CursorPageBaseReq;
import com.kkuil.blackchat.domain.vo.response.CursorPageBaseResp;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author Kkuil
 * @Date 2023/10/1 18:15
 * @Description 游标分页工具类
 */
public class CursorUtil {

    public static <T> CursorPageBaseResp<Pair<T, Double>, String> getCursorPageByRedis(
            CursorPageBaseReq<String> cursorPageBaseReq,
            String redisKey,
            Function<String, T> typeConvert
    ) {
        Set<ZSetOperations.TypedTuple<String>> typedTuples;
        if (StrUtil.isBlank(cursorPageBaseReq.getCursor())) {
            // 第一次
            typedTuples = RedisUtil.zReverseRangeWithScores(redisKey, cursorPageBaseReq.getPageSize());
        } else {
            typedTuples = RedisUtil.zReverseRangeByScoreWithScores(redisKey, Double.parseDouble(cursorPageBaseReq.getCursor()), cursorPageBaseReq.getPageSize());
        }
        List<Pair<T, Double>> result = typedTuples
                .stream()
                .map(t -> Pair.of(typeConvert.apply(t.getValue()), t.getScore()))
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                .collect(Collectors.toList());
        String cursor = Optional.ofNullable(CollectionUtil.getLast(result))
                .map(Pair::getValue)
                .map(String::valueOf)
                .orElse(null);
        Boolean isLast = result.size() != cursorPageBaseReq.getPageSize();
        return new CursorPageBaseResp<>(cursor, isLast, result, null);
    }

    public static <T, C> CursorPageBaseResp<T, String> getCursorPageByMysql(
            IService<T> mapper,
            CursorPageBaseReq<String> request,
            Consumer<LambdaQueryWrapper<T>> initWrapper,
            SFunction<T, ?> cursorColumn
    ) {
        Class<?> cursorType = LambdaUtil.getReturnType(cursorColumn);
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        initWrapper.accept(wrapper);
        if (StrUtil.isNotBlank(request.getCursor())) {
            wrapper.lt(cursorColumn, parseCursor(request.getCursor(), cursorType));
        }
        wrapper.orderByDesc(cursorColumn);
        Page<T> page = mapper.page(request.plusPage(), wrapper);
        String cursor = Optional.ofNullable(CollectionUtil.getLast(page.getRecords()))
                .map(cursorColumn)
                .map(CursorUtil::toCursor)
                .orElse(null);
        Boolean isLast = page.getRecords().size() < request.getPageSize();
        return new CursorPageBaseResp<>(cursor, isLast, page.getRecords(), null);
    }

    private static String toCursor(Object o) {
        if (o instanceof Date) {
            return String.valueOf(((Date) o).getTime());
        } else {
            return o.toString();
        }
    }

    private static Object parseCursor(String cursor, Class<?> cursorClass) {
        if (Date.class.isAssignableFrom(cursorClass)) {
            return new Date(Long.parseLong(cursor));
        } else {
            return cursor;
        }
    }
}
