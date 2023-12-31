package com.kkuil.blackchat.cache;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.kkuil.blackchat.constant.RedisKeyConst;
import com.kkuil.blackchat.core.user.domain.vo.request.UserInfoCache;
import com.kkuil.blackchat.dao.UserDAO;
import com.kkuil.blackchat.domain.dto.UserBaseInfo;
import com.kkuil.blackchat.domain.entity.User;
import com.kkuil.blackchat.utils.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    @Resource
    @Lazy
    private UserDAO userDao;

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

    /**
     * 获取基本用户信息
     *
     * @param uid 用户ID
     * @return 用户基本信息
     */
    public UserBaseInfo getBaseUserInfoByUid(Long uid) {
        // 1. 先尝试从缓存中获取
        String key = String.format(RedisKeyConst.USER_INFO_STRING, uid);
        String userKey = RedisKeyConst.getKey(key);
        UserBaseInfo userBaseInfo = RedisUtil.get(userKey, UserBaseInfo.class);

        if (ObjectUtil.isNotNull(userBaseInfo)) {
            return userBaseInfo;
        }

        // 2. 从数据库中获取数据
        User user = userDao.getById(uid);
        UserBaseInfo userInfo = BeanUtil.toBean(user, UserBaseInfo.class);

        // 3. 缓存数据
        RedisUtil.set(userKey, userInfo);
        return userInfo;
    }

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     */
    public void updateUserInfo(User user) {
        Long uid = user.getId();
        String key = String.format(RedisKeyConst.USER_INFO_STRING, uid);
        String userKey = RedisKeyConst.getKey(key);
        UserBaseInfo userBaseInfo = BeanUtil.toBean(user, UserBaseInfo.class);
        RedisUtil.set(userKey, userBaseInfo);
    }

    /**
     * 获取总人数
     *
     * @return 总人数
     */
    public Long getTotalCount() {
        String key = RedisKeyConst.getKey(RedisKeyConst.USER_TOTAL_COUNT_STRING, "");

        Long totalCount = RedisUtil.get(key, Long.class);

        if (ObjectUtil.isNull(totalCount)) {
            List<User> list = userDao.list();
            RedisUtil.set(key, list.size());
            return (long) list.size();
        }

        return totalCount;
    }

    /**
     * 批量获取用户基本信息
     *
     * @param uidList 用户ID列表
     * @return 用户ID列表
     */
    public List<UserInfoCache> getBatchByUidList(List<Long> uidList) {
        List<UserInfoCache> userInfoCaches = new ArrayList<>();
        uidList.forEach(uid -> {
            UserBaseInfo baseInfo = this.getBaseUserInfoByUid(uid);
            UserInfoCache userInfoCache = BeanUtil.toBean(baseInfo, UserInfoCache.class);
            userInfoCaches.add(userInfoCache);
        });
        return userInfoCaches;
    }

    /**
     * 是否存在用户
     *
     * @param list 用户ID列表
     * @return 是否存在
     */
    public Boolean isExistUsers(List<Long> list) {
        boolean isExist = true;
        for (Long uid : list) {
            UserBaseInfo userBaseInfo = getBaseUserInfoByUid(uid);
            if (ObjectUtil.isNull(userBaseInfo)) {
                isExist = false;
            }
        }
        return isExist;
    }
}
