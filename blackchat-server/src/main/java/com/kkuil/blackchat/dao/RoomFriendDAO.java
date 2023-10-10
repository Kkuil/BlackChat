package com.kkuil.blackchat.dao;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.RoomFriend;
import com.kkuil.blackchat.mapper.RoomFriendMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/9/28 15:02
 * @Description 单聊房间表访问层
 */
@Service
public class RoomFriendDAO extends ServiceImpl<RoomFriendMapper, RoomFriend> {

    @Resource
    private RoomDAO roomDao;

    @Resource
    private ContactDAO contactDao;

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
     * @param uid    用户id
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

    /**
     * 通过房间ID获取信息
     *
     * @param roomId 房间ID
     * @return 信息
     */
    public RoomFriend getUidByRoomId(Long roomId) {
        return this.lambdaQuery()
                .eq(RoomFriend::getRoomId, roomId)
                .select(RoomFriend::getUid1, RoomFriend::getUid2)
                .one();
    }

    /**
     * 批量获取朋友
     *
     * @param uid 用户ID
     * @return 朋友列表ID
     */
    public List<Long> listFriendIdByUid(Long uid) {
        return this.lambdaQuery()
                .eq(RoomFriend::getUid1, uid)
                .or()
                .eq(RoomFriend::getUid2, uid).list().stream()
                .map(roomFriend -> {
                    if (roomFriend.getUid1().equals(uid)) {
                        return roomFriend.getUid2();
                    } else {
                        return roomFriend.getUid1();
                    }
                }).toList();
    }

    /**
     * 删除好友
     *
     * @param uid      用户ID
     * @param friendId 朋友ID
     * @return 是否删除成功
     */
    public Boolean delFriend(Long uid, Long friendId) {
        String roomKey;
        if(uid < friendId) {
            roomKey = uid + "_" + friendId;
        } else {
            roomKey = friendId + "_" + uid;
        }
        LambdaQueryWrapper<RoomFriend> wrapper = new QueryWrapper<RoomFriend>().lambda()
                .eq(RoomFriend::getRoomKey, roomKey);
        // 删除房间
        RoomFriend roomFriend = this.getOne(wrapper);
        Long roomId = roomFriend.getRoomId();
        roomDao.deleteById(roomId);

        // 删除会话
        contactDao.deleteByRoomId(roomId);
        return this.remove(wrapper);
    }
}
