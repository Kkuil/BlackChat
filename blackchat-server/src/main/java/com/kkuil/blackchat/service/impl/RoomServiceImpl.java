package com.kkuil.blackchat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.kkuil.blackchat.core.chat.domain.enums.RoomTypeEnum;
import com.kkuil.blackchat.core.contact.domain.bo.GroupMemberBaseInfo;
import com.kkuil.blackchat.core.contact.domain.enums.GroupRoleEnum;
import com.kkuil.blackchat.dao.*;
import com.kkuil.blackchat.domain.entity.Message;
import com.kkuil.blackchat.domain.entity.Room;
import com.kkuil.blackchat.domain.enums.error.ChatErrorEnum;
import com.kkuil.blackchat.service.RoomService;
import com.kkuil.blackchat.utils.AssertUtil;
import com.kkuil.blackchat.core.websocket.domain.vo.request.ChatMessageReq;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    private ContactDAO contactDao;

    @Resource
    private MessageDAO messageDao;

    @Resource
    private RoomGroupDAO roomGroupDao;

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
                hasUser = roomFriendDao.isFriend(Arrays.asList(uids));
            }
            AssertUtil.isTrue(hasUser, ChatErrorEnum.NOT_FRIEND.getMsg());
        } else {
            // 2.2 群聊检查
            Boolean hasUser = groupMemberDao.isGroupShip(roomId, CollectionUtil.toList(uids));
            AssertUtil.isTrue(hasUser, ChatErrorEnum.NOT_IN_GROUP.getMsg());
        }
        return true;
    }

    /**
     * 创建群聊
     *
     * @param uid       用户ID
     * @param groupName 群名
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createGroup(Long uid, String groupName, String groupAvatar) {
        // 1. 创建房间（room）
        Room room = roomDao.createRoom(RoomTypeEnum.GROUP);
        Long roomId = room.getId();

        // 2. 创建首条消息（message）
        Message message = messageDao.createFristCreateGroupMessage(uid, roomId, groupName);

        // 2.1 补充房间的最新消息
        roomDao.updateRoomNewestMsg(roomId, message.getCreateTime(), message.getId());

        // 3. 将群主加入房间，并设置为群主（group_member）
        List<GroupMemberBaseInfo> list = Collections.singletonList(new GroupMemberBaseInfo(uid, GroupRoleEnum.MASTER.getId()));
        groupMemberDao.createGroup(roomId, groupName, list);

        // 4. 设置群名等信息（room_group）
        roomGroupDao.createGroup(roomId, groupName, groupAvatar);

        // 5. 给群主创建会话（contact）
        contactDao.createContact(uid, roomId, message.getCreateTime());

        return roomId;
    }

}
