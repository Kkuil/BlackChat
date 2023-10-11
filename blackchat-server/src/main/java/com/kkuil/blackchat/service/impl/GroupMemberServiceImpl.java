package com.kkuil.blackchat.service.impl;

import com.kkuil.blackchat.cache.UserCache;
import com.kkuil.blackchat.core.user.domain.vo.request.CreateGroupReq;
import com.kkuil.blackchat.core.user.domain.vo.response.UserSearchRespVO;
import com.kkuil.blackchat.dao.*;
import com.kkuil.blackchat.domain.entity.Room;
import com.kkuil.blackchat.domain.enums.ChatMessageEnum;
import com.kkuil.blackchat.domain.enums.UserMessageEnum;
import com.kkuil.blackchat.domain.enums.error.ChatErrorEnum;
import com.kkuil.blackchat.domain.enums.error.CommonErrorEnum;
import com.kkuil.blackchat.domain.enums.error.UserErrorEnum;
import com.kkuil.blackchat.service.GroupMemberService;
import com.kkuil.blackchat.utils.AssertUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/10/9 19:38
 * @Description 针对表【group_member(群成员表)】的数据库操作Service实现
 */
@Service
public class GroupMemberServiceImpl implements GroupMemberService {

    @Resource
    private GroupMemberDAO groupMemberDao;

    @Resource
    private ContactDAO contactDao;

    @Resource
    private MessageDAO messageDao;

    @Resource
    private RoomDAO roomDao;

    @Resource
    private UserCache userCache;

    @Resource
    private RoomFriendDAO roomFriendDao;

    @Resource
    private UserApplyDAO userApplyDao;

    /**
     * 退出群组
     *
     * @param uid     用户ID
     * @param groupId 群组ID
     * @return 是否退出
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String exitGroup(Long uid, Long groupId) {
        Boolean isGroupShip = groupMemberDao.isGroupShip(groupId, uid);
        AssertUtil.isTrue(isGroupShip, ChatErrorEnum.NOT_IN_GROUP.getMsg());

        Room room = roomDao.getById(groupId);

        if (room.isHotRoom()) {
            return ChatErrorEnum.NOT_ALLOWED_TO_EXIT_ALL_GROUP.getErrorMsg();
        }

        // 删除成员
        Boolean isExitGroup = groupMemberDao.exitGroup(groupId, uid);
        AssertUtil.isTrue(isExitGroup, CommonErrorEnum.SYSTEM_ERROR.getMsg());

        // 删除会话记录
        Boolean isDelContact = contactDao.delContact(groupId, uid);
        AssertUtil.isTrue(isDelContact, CommonErrorEnum.SYSTEM_ERROR.getMsg());

        // 删除消息记录
        Boolean isDelMsg = messageDao.delRecordBatch(groupId, uid);
        AssertUtil.isTrue(isDelMsg, CommonErrorEnum.SYSTEM_ERROR.getMsg());

        return ChatMessageEnum.EXIT_GROUP_SUCCESS.getMsg();
    }

    /**
     * 创建群聊
     *
     * @param uid     申请人ID
     * @param uidList 邀请人ID
     * @return 是否创建成功
     */
    @Override
    public String createGroup(Long uid, CreateGroupReq createGroupReq) {
        List<Long> uidList = createGroupReq.getUidList();
        AssertUtil.isTrue(uidList.size() > 2, ChatErrorEnum.NOT_ALLOWED_COUNT_GROUP.getMsg());

        String msg = createGroupReq.getMsg();

        // 1. 判断用户是否存在
        Boolean isExistUsers = userCache.isExistUsers(uidList);
        AssertUtil.isTrue(isExistUsers, UserErrorEnum.USER_NOT_EXIST.getMsg());

        // 2. 判断是否已经是好友关系
        List<UserSearchRespVO> friend = roomFriendDao.isFriend(uid, uidList);
        friend.forEach(user -> {
            AssertUtil.isTrue(user.getIsFriend(), UserErrorEnum.NOT_FRIEND.getMsg());
        });

        // 3. 创建群聊
        Boolean isAddFriend = userApplyDao.createGroup(uid, uidList, msg);
        AssertUtil.isTrue(isAddFriend, UserErrorEnum.COMMIT_APPLY_FAIL.getMsg());

        return UserMessageEnum.COMMIT_APPLY_SUCESS.getMsg();
    }

}
