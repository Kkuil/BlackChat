package com.kkuil.blackchat.cache;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.kkuil.blackchat.constant.RedisKeyConst;
import com.kkuil.blackchat.dao.RoomFriendDAO;
import com.kkuil.blackchat.domain.bo.room.FriendBaseInfo;
import com.kkuil.blackchat.domain.entity.RoomFriend;
import com.kkuil.blackchat.utils.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @Author Kkuil
 * @Date 2023/10/5 20:12
 * @Description 单聊缓存
 */
@Component
public class RoomFriendCache {

    @Resource
    private RoomFriendDAO roomFriendDao;

    /**
     * 通过房间ID获取单聊信息
     *
     * @param roomId 房间ID
     * @return 单聊信息
     */
    public FriendBaseInfo getBaseInfoById(Long roomId) {
        String key = RedisKeyConst.getKey(RedisKeyConst.FRIEND_INFO_STRING, roomId);
        FriendBaseInfo baseInfo = RedisUtil.get(key, FriendBaseInfo.class);

        if (ObjectUtil.isNotNull(baseInfo)) {
            return baseInfo;
        }

        // 查询数据库
        RoomFriend roomFriend = roomFriendDao.getByRoomId(roomId);
        FriendBaseInfo friendBaseInfo = BeanUtil.toBean(roomFriend, FriendBaseInfo.class);
        RedisUtil.set(key, friendBaseInfo);
        return friendBaseInfo;
    }

}
