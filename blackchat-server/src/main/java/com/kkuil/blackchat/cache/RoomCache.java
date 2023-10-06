package com.kkuil.blackchat.cache;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.kkuil.blackchat.constant.RedisKeyConst;
import com.kkuil.blackchat.dao.RoomDAO;
import com.kkuil.blackchat.domain.bo.RoomBaseInfo;
import com.kkuil.blackchat.utils.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @Author Kkuil
 * @Date 2023/10/5 19:27
 * @Description 房间缓存
 */
@Component
public class RoomCache {

    @Resource
    private RoomDAO roomDao;

    public RoomBaseInfo getRoomBaseInfoById(Long roomId) {
        String key = RedisKeyConst.getKey(RedisKeyConst.ROOM_INFO_STRING, roomId);
        RoomBaseInfo roomMap = RedisUtil.get(key, RoomBaseInfo.class);

        if (ObjectUtil.isNotNull(roomMap)) {
            return roomMap;
        }

        // 查询数据库
        RoomBaseInfo roomBaseInfo = BeanUtil.toBean(roomDao.getById(roomId), RoomBaseInfo.class);
        RedisUtil.set(key, roomBaseInfo);
        return roomBaseInfo;
    }

}
