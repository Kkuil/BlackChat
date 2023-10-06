package com.kkuil.blackchat.dao;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.RoomFriend;
import com.kkuil.blackchat.mapper.RoomFriendMapper;
import org.springframework.stereotype.Service;

/**
 * @Author Kkuil
 * @Date 2023/9/28 15:02
 * @Description 单聊房间表访问层
 */
@Service
public class RoomFriendDAO extends ServiceImpl<RoomFriendMapper, RoomFriend> {

    /**
     * 判断房间内是否有该用户
     *
     * @param roomId 房间ID
     * @param uid    用户ID
     * @return 是否有该用户
     */
    public Boolean hasUser(Long roomId, Long uid) {
        LambdaQueryChainWrapper<RoomFriend> wrapper = lambdaQuery().eq(RoomFriend::getRoomId, roomId)
                .eq(RoomFriend::getUid1, uid);
        RoomFriend roomFriend = this.getOne(wrapper);
        return ObjectUtil.isNotNull(roomFriend);
    }

    /**
     * 判断用户是否是朋友关系
     *
     * @param roomId 房间ID
     * @param uid 用户id
     * @return 是否是朋友关系
     */
    public Boolean isFriend(Long roomId, Long uid) {
        RoomFriend roomFriend = lambdaQuery()
                .eq(RoomFriend::getRoomId, roomId)
                .eq(RoomFriend::getUid1, uid)
                .or()
                .eq(RoomFriend::getRoomId, roomId)
                .eq(RoomFriend::getUid2, uid)
                .one();
        return ObjectUtil.isNotNull(roomFriend);
    }

    /**
     * 判断用户是否是朋友关系
     *
     * @param uids 用户id
     * @return 是否是朋友关系
     */
    public Boolean isFriend(Long... uids) {
        Long uid1;
        Long uid2;
        if (uids[0] > uids[1]) {
            uid1 = uids[1];
            uid2 = uids[0];
        } else {
            uid1 = uids[0];
            uid2 = uids[1];
        }
        RoomFriend roomFriend = lambdaQuery()
                .eq(RoomFriend::getUid1, uid1)
                .eq(RoomFriend::getUid2, uid2)
                .one();
        return ObjectUtil.isNotNull(roomFriend);
    }

    /**
     * 通过房间ID获取信息
     *
     * @param roomId 房间ID
     * @return 信息
     */
    public RoomFriend getByRoomId(Long roomId) {
        return lambdaQuery()
                .eq(RoomFriend::getRoomId, roomId)
                .one();
    }
}
