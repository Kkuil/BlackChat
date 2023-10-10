package com.kkuil.blackchat.service.impl;

import com.kkuil.blackchat.dao.*;
import com.kkuil.blackchat.domain.entity.Room;
import com.kkuil.blackchat.domain.enums.error.ChatErrorEnum;
import com.kkuil.blackchat.service.RoomService;
import com.kkuil.blackchat.utils.AssertUtil;
import com.kkuil.blackchat.core.websocket.domain.vo.request.ChatMessageReq;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author Kkuil
 * @Date 2023/9/28 14:52
 * @Description 针对表【room(房间表)】的数据库操作Service实现
 */
@Service
public class RoomServiceImpl implements RoomService {

    @Resource
    private RoomDAO roomDao;

    @Resource
    private RoomFriendDAO roomFriendDao;

    @Resource
    private GroupMemberDAO groupMemberDao;

    /**
     * 检查用户是否存在该房间，该房间是否把他拉黑或者房间是否存在
     *
     * @param uid            用户ID
     * @param chatMessageReq 请求消息体
     */
    @Override
    public void check(Long uid, ChatMessageReq chatMessageReq) {
        // -1. 判断是否是全体成员，跳过
        if (uid.equals(ChatGroupSpecialMemberEnum.ALL.getId())) {
            return;
        }
        Long roomId = chatMessageReq.getRoomId();

        Room room = roomDao.getById(roomId);
        AssertUtil.isNotEmpty(room, ChatErrorEnum.ROOM_NOT_EXIST.getMsg());
        // 0. 如果是大群聊跳过校验
        if (room.isHotRoom()) {
            return;
        }
        // 1. 检查房间是否存在
        AssertUtil.isNotEmpty(room, ChatErrorEnum.ROOM_NOT_EXIST.getMsg());
        // 2. 检查该房间内是否有该用户
        this.checkRoomMembership(roomId, uid);
    }

    /**
     * 判断两个用户是否在存在于同一个房间
     *
     * @param roomId 房间ID
     * @param uids   一群用户ID
     */
    @Override
    public Boolean checkRoomMembership(Long roomId, Long... uids) {
        // -1. 判断传递的用户ID
        int length = uids.length;
        if (length == 0) {
            return false;
        }
        Room room = roomDao.getById(roomId);

        // 0. 如果是大群聊跳过校验
        if (room.isHotRoom()) {
            return true;
        }

        // 1. 检查房间是否存在
        AssertUtil.isNotEmpty(room, ChatErrorEnum.ROOM_NOT_EXIST.getMsg());

        // 2. 检查该房间内是否有该用户
        if (room.isRoomFriend()) {
            // 2.1 单聊检查
            Boolean hasUser;
            if (uids.length == 1) {
                hasUser = roomFriendDao.isFriend(roomId, uids[0]);
            } else {
                hasUser = roomFriendDao.isFriend(uids);
            }
            AssertUtil.isTrue(hasUser, ChatErrorEnum.NOT_FRIEND.getMsg());
        } else {
            // 2.2 群聊检查
            Boolean hasUser = groupMemberDao.isGroupShip(roomId, uids);
            AssertUtil.isTrue(hasUser, ChatErrorEnum.NOT_IN_GROUP.getMsg());
        }
        return true;
    }

}
