package com.kkuil.blackchat.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.kkuil.blackchat.cache.UserCache;
import com.kkuil.blackchat.core.chat.domain.enums.RoomTypeEnum;
import com.kkuil.blackchat.core.contact.domain.vo.response.FriendResp;
import com.kkuil.blackchat.core.user.domain.vo.request.AddFriendReq;
import com.kkuil.blackchat.dao.*;
import com.kkuil.blackchat.domain.dto.IpDetail;
import com.kkuil.blackchat.domain.dto.IpInfo;
import com.kkuil.blackchat.domain.entity.Message;
import com.kkuil.blackchat.domain.entity.Room;
import com.kkuil.blackchat.domain.entity.User;
import com.kkuil.blackchat.domain.entity.UserApply;
import com.kkuil.blackchat.domain.enums.UserMessageEnum;
import com.kkuil.blackchat.domain.enums.error.CommonErrorEnum;
import com.kkuil.blackchat.domain.enums.error.UserErrorEnum;
import com.kkuil.blackchat.event.MessageSendEvent;
import com.kkuil.blackchat.service.RoomFriendService;
import com.kkuil.blackchat.utils.AssertUtil;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @Author Kkuil
 * @Date 2023/10/10 14:14
 * @Description 针对表【room_friend(单聊房间表)】的数据库操作Service实现
 */
@Service
public class RoomFriendServiceImpl implements RoomFriendService {

    @Resource
    private RoomFriendDAO roomFriendDao;

    @Resource
    private UserCache userCache;

    @Resource
    private UserDAO userDao;

    @Resource
    private ContactDAO contactDao;

    @Resource
    private MessageDAO messageDao;

    @Resource
    private UserApplyDAO userApplyDao;

    @Resource
    private RoomDAO roomDao;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * 获取朋友列表
     *
     * @param uid 用户ID
     * @return 朋友列表
     */
    @Override
    public List<FriendResp> listFriend(Long uid) {
        List<Long> friendUidList = roomFriendDao.listFriendIdByUid(uid);
        List<User> userList = userDao.getBath(friendUidList);

        return userList.stream().map(user -> {
            FriendResp friendResp = new FriendResp();
            friendResp.setUid(user.getId());

            Long roomId = roomFriendDao.getRoomIdByUid(uid, user.getId());
            friendResp.setRoomId(roomId);

            IpInfo ipInfo = Optional.ofNullable(user.getIpInfo()).orElse(new IpInfo());
            if (ObjectUtil.isNull(ipInfo)) {
                friendResp.setPlace("未知");
            } else {
                IpDetail ipDetail = Optional.ofNullable(ipInfo.getUpdateIpDetail()).orElse(new IpDetail());
                if (ObjectUtil.isNull(ipDetail)) {
                    friendResp.setPlace("未知");
                } else {
                    friendResp.setPlace(ipDetail.getCity());
                }
            }
            return friendResp;
        }).toList();
    }

    /**
     * 删除用户
     *
     * @param uid      用户ID
     * @param friendId 朋友ID
     * @return 是否删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delFriend(Long uid, Long friendId) {
        // 1. 判断用户是否存在
        Boolean isExist = userCache.isExistUsers(Collections.singletonList(friendId));
        AssertUtil.isTrue(isExist, UserErrorEnum.USER_NOT_EXIST.getMsg());

        // 2. 删除房间
        Long roomId = roomFriendDao.delFriend(uid, friendId);
        AssertUtil.isNotEmpty(roomId, CommonErrorEnum.SYSTEM_ERROR.getMsg());

        // 3. 删除会话
        contactDao.deleteByRoomId(roomId);

        // 4. 删除聊天记录
        messageDao.deleteByRoomId(roomId);

        return UserMessageEnum.DEL_FRIEND_SUCCESS.getMsg();
    }

    /**
     * 加好友
     *
     * @param uid          用户ID
     * @param addFriendReq AddFriendReq
     * @return 是否添加成功
     */
    @Override
    public String applyAddFriend(Long uid, AddFriendReq addFriendReq) {
        Long repliedId = addFriendReq.getRepliedId();
        String msg = addFriendReq.getMsg();

        // 1. 判断用户是否存在
        Boolean isExistUsers = userCache.isExistUsers(Collections.singletonList(repliedId));
        AssertUtil.isTrue(isExistUsers, UserErrorEnum.USER_NOT_EXIST.getMsg());

        // 2. 判断是否已经是好友关系
        Boolean isFriend = roomFriendDao.isFriend(Arrays.asList(uid, repliedId));
        AssertUtil.isFalse(isFriend, UserErrorEnum.ALREADY_FRIEND.getMsg());

        // 2. 判断是否已经是提交过了好友申请
        Boolean isApplying = userApplyDao.isApplyingFriend(uid, repliedId);
        AssertUtil.isFalse(isApplying, UserErrorEnum.REPEAT_APPLY.getMsg());

        // 3. 添加好友
        Boolean isAddFriend = userApplyDao.addFriend(uid, repliedId, msg);
        AssertUtil.isTrue(isAddFriend, UserErrorEnum.COMMIT_APPLY_FAIL.getMsg());

        return UserMessageEnum.COMMIT_APPLY_SUCESS.getMsg();
    }

    /**
     * 同意加好友
     *
     * @param userApply 好友申请记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void agreeAddFriend(UserApply userApply) {
        // 1. 创建房间（room）
        Room room = roomDao.createRoom(RoomTypeEnum.FRIEND);
        Long roomId = room.getId();

        // 2. 生成好友欢迎消息（message）
        Long uid = userApply.getUid();
        Long targetId = userApply.getTargetId();
        Message message = messageDao.createFristAddFriendMessage(roomId, uid, targetId);

        // 2.1 补充房间的最新消息
        roomDao.updateRoomNewestMsg(roomId, message.getCreateTime(), message.getId());

        // 3. 创建会话并更新最新消息（contact）
        contactDao.createContact(uid, roomId, message.getCreateTime());
        contactDao.createContact(targetId, roomId, message.getCreateTime());

        // 4. 创建好友关系
        roomFriendDao.addFriend(roomId, uid, targetId);

        // 5. 发布消息事件
        applicationEventPublisher.publishEvent(new MessageSendEvent(this, message));
    }
}
