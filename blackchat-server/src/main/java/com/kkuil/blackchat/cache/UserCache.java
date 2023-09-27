package com.kkuil.blackchat.cache;

import com.kkuil.blackchat.constant.RedisKeyConst;
import com.kkuil.blackchat.utils.RedisUtil;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author Kkuil
 * @Date 2023/9/27 11:26
 * @Description 用户缓存
 */
@Component
public class UserCache {

    /**
     * 用户上线
     *
     * @param uid     用户ID
     * @param optTime 上线时间
     */
    public void online(Long uid, Date optTime) {
        String onlineKey = RedisKeyConst.getKey(RedisKeyConst.ONLINE_UID_ZET);
        String offlineKey = RedisKeyConst.getKey(RedisKeyConst.OFFLINE_UID_ZET);
        // 移除离线表
        RedisUtil.zRemove(offlineKey, uid);
        // 更新上线表
        RedisUtil.zAdd(onlineKey, uid, optTime.getTime());
    }

    /**
     * 用户上线
     *
     * @param uid     用户ID
     * @param optTime 上线时间
     */
    public void offline(Long uid, Date optTime) {
        String onlineKey = RedisKeyConst.getKey(RedisKeyConst.ONLINE_UID_ZET);
        String offlineKey = RedisKeyConst.getKey(RedisKeyConst.OFFLINE_UID_ZET);
        // 移除上线线表
        RedisUtil.zRemove(onlineKey, uid);
        // 更新上线表
        RedisUtil.zAdd(offlineKey, uid, optTime.getTime());
    }

    /**
     * 获取用户上线列表
     *
     * @return 上线用户ID的列表
     */
    public List<Long> getOnlineUidList() {
        String onlineKey = RedisKeyConst.getKey(RedisKeyConst.ONLINE_UID_ZET);
        Set<String> strings = RedisUtil.zAll(onlineKey);
        return strings.stream().map(Long::parseLong).collect(Collectors.toList());
    }

}
