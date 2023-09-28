package com.kkuil.blackchat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.kkuil.blackchat.dao.RoomDAO;
import com.kkuil.blackchat.dao.RoomFriendDAO;
import com.kkuil.blackchat.dao.RoomGroupDAO;
import com.kkuil.blackchat.domain.entity.Room;
import com.kkuil.blackchat.domain.enums.error.ChatErrorEnum;
import com.kkuil.blackchat.service.RoomService;
import com.kkuil.blackchat.utils.AssertUtil;
import com.kkuil.blackchat.web.chat.domain.enums.RoomTypeEnum;
import com.kkuil.blackchat.web.websocket.domain.dto.chat.AbstractChatMessageBaseReq;
import com.kkuil.blackchat.web.websocket.domain.vo.request.ChatMessageReq;
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
    private RoomGroupDAO roomGroupDao;

    /**
     * 检查用户是否存在该房间，该房间是否把他拉黑或者房间是否存在
     *
     * @param uid            用户ID
     * @param chatMessageReq 请求消息体
     */
    @Override
    public void check(Long uid, ChatMessageReq<? extends AbstractChatMessageBaseReq> chatMessageReq) {
        // -1. 判断是否是全体成员，跳过
        if (uid.equals(ChatGroupSpecialMemberEnum.ALL.getId())) {
            return;
        }
        Long roomId = chatMessageReq.getRoomId();
        Room room = roomDao.getById(roomId);
        // 0. 如果是大群聊跳过校验
        Integer hotFlag = room.getHotFlag();
        if (hotFlag == 1) {
            return;
        }
        // 1. 检查房间是否存在
        AssertUtil.isNotEmpty(room, ChatErrorEnum.ROOM_NOT_EXIST.getMsg());
        // 2. 检查该房间内是否有该用户
        Integer roomType = room.getType();
        if (roomType.equals(RoomTypeEnum.FRIEND.getType())) {
            // 2.1 单聊检查
            Boolean hasUser = roomFriendDao.hasUser(roomId, uid);
            AssertUtil.isTrue(hasUser, ChatErrorEnum.NOT_FRIEND.getMsg());
        } else {
            // 2.2 群聊检查
            Boolean hasUser = roomGroupDao.hasUser(roomId, uid);
            AssertUtil.isTrue(hasUser, ChatErrorEnum.NOT_IN_GROUP.getMsg());
        }
    }

    /**
     * 判断两个用户是否在存在于同一个房间
     *
     * @param roomId 房间ID
     * @param uids   一群用户ID
     */
    @Override
    public Boolean checkRoomMembership(Long roomId, Long... uids) {
        // -1. 判断是否包含全体用户
        boolean isContainAll = CollectionUtil.toList(uids).contains(ChatGroupSpecialMemberEnum.ALL.getId());
        if (isContainAll) {
            return true;
        }
        Room room = roomDao.getById(roomId);
        // 0. 如果是大群聊跳过校验
        Integer hotFlag = room.getHotFlag();
        if (hotFlag == 1) {
            return true;
        }
        // 1. 检查房间是否存在
        AssertUtil.isNotEmpty(room, ChatErrorEnum.ROOM_NOT_EXIST.getMsg());
        // 2. 检查该房间内是否有该用户
        Integer roomType = room.getType();
        if (roomType.equals(RoomTypeEnum.FRIEND.getType())) {
            // 2.1 单聊检查
            int length = uids.length;
            AssertUtil.isTrue(length == 2, ChatErrorEnum.PEOPLE_COUNT_NOT_MATCHE.getMsg());
            Boolean hasUser = roomFriendDao.isFriend(uids);
            AssertUtil.isTrue(hasUser, ChatErrorEnum.NOT_FRIEND.getMsg());
        } else {
            // 2.2 群聊检查
            Boolean hasUser = roomGroupDao.isGroupShip(roomId, uids);
            AssertUtil.isTrue(hasUser, ChatErrorEnum.NOT_IN_GROUP.getMsg());
        }
        return true;
    }

}
