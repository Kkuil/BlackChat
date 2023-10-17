package com.kkuil.blackchat.dao;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.constant.RedisKeyConst;
import com.kkuil.blackchat.core.contact.domain.enums.FriendStatusEnum;
import com.kkuil.blackchat.core.user.domain.vo.response.UserSearchRespVO;
import com.kkuil.blackchat.domain.bo.room.FriendBaseInfo;
import com.kkuil.blackchat.domain.entity.RoomFriend;
import com.kkuil.blackchat.mapper.RoomFriendMapper;
import com.kkuil.blackchat.utils.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
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
    public Boolean isFriend(List<Long> uids) {
        String roomKey;
        Long uid1 = uids.get(0);
        Long uid2 = uids.get(1);
        if (uid1 > uid2) {
            roomKey = uid2 + "_" + uid1;
        } else {
            roomKey = uid1 + "_" + uid2;
        }
        RoomFriend roomFriend = lambdaQuery()
                .eq(RoomFriend::getRoomKey, roomKey)
                .one();
        return ObjectUtil.isNotNull(roomFriend);
    }

    /**
     * 判断用户是否是朋友关系
     *
     * @param uids 用户id
     * @return 是否是朋友关系
     */
    public List<UserSearchRespVO> isFriend(Long uid, List<Long> uidList) {
        List<UserSearchRespVO> list = new ArrayList<>();
        Boolean isLogin = ObjectUtil.isNotNull(uid);
        for (Long id : uidList) {
            UserSearchRespVO userSearchRespVO = new UserSearchRespVO();
            if (isLogin) {
                Boolean isFriend = this.isFriend(Arrays.asList(uid, id));
                userSearchRespVO.setUid(id);
                userSearchRespVO.setIsFriend(isFriend);
            } else {
                userSearchRespVO.setIsFriend(false);
            }
            list.add(userSearchRespVO);
        }
        return list;
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
                .eq(RoomFriend::getUid2, uid).list()
                .stream()
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
    public Long delFriend(Long uid, Long friendId) {
        String roomFriendKey = this.getRoomKey(uid, friendId);
        LambdaQueryWrapper<RoomFriend> wrapper = new QueryWrapper<RoomFriend>().lambda()
                .eq(RoomFriend::getRoomKey, roomFriendKey);
        // 删除房间
        RoomFriend roomFriend = this.getOne(wrapper);
        Long roomId = roomFriend.getRoomId();
        roomDao.deleteById(roomId);
        this.remove(wrapper);

        // 同步删除缓存
        String friendKey = String.format(RedisKeyConst.FRIEND_INFO_STRING, roomId);
        String roomKey = String.format(RedisKeyConst.ROOM_INFO_STRING, roomId);
        RedisUtil.del(friendKey, roomKey);

        return roomId;
    }

    /**
     * 建立好友关系
     *
     * @param roomId   房间ID
     * @param uid      用户1ID
     * @param targetId 用户2ID
     */
    public void addFriend(Long roomId, Long uid, Long targetId) {
        String roomKey = this.getRoomKey(uid, targetId);
        Long uid1;
        Long uid2;
        if (uid < targetId) {
            uid1 = uid;
            uid2 = targetId;
        } else {
            uid1 = targetId;
            uid2 = uid;
        }
        RoomFriend roomFriend = new RoomFriend();
        roomFriend.setRoomId(roomId);
        roomFriend.setUid1(uid1);
        roomFriend.setUid2(uid2);
        roomFriend.setRoomKey(roomKey);
        roomFriend.setStatus(FriendStatusEnum.NORMAL.getCode());
        this.save(roomFriend);
    }

    /**
     * 通过用户ID获取房间ID
     *
     * @param uid1 用户1ID
     * @param uid2 用户2ID
     * @return 房间ID
     */
    public Long getRoomIdByUid(Long uid1, Long uid2) {
        String roomKey = this.getRoomKey(uid1, uid2);
        RoomFriend roomFriend = this.lambdaQuery()
                .eq(RoomFriend::getRoomKey, roomKey)
                .one();
        return roomFriend.getRoomId();
    }

    /**
     * 获取房间Key
     *
     * @param uid1 用户1ID
     * @param uid2 用户2ID
     * @return 房间Key
     */
    public String getRoomKey(Long uid1, Long uid2) {
        String roomKey;
        if (uid1 < uid2) {
            roomKey = uid1 + "_" + uid2;
        } else {
            roomKey = uid2 + "_" + uid1;
        }
        return roomKey;
    }
}
