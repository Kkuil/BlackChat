package com.kkuil.blackchat.service.impl;

import com.kkuil.blackchat.cache.UserCache;
import com.kkuil.blackchat.constant.GroupConst;
import com.kkuil.blackchat.core.user.domain.vo.request.CreateGroupReq;
import com.kkuil.blackchat.core.user.domain.vo.response.UserSearchRespVO;
import com.kkuil.blackchat.dao.*;
import com.kkuil.blackchat.domain.entity.Room;
import com.kkuil.blackchat.domain.entity.UserApply;
import com.kkuil.blackchat.domain.enums.ChatMessageEnum;
import com.kkuil.blackchat.domain.enums.UserMessageEnum;
import com.kkuil.blackchat.domain.enums.error.ChatErrorEnum;
import com.kkuil.blackchat.domain.enums.error.CommonErrorEnum;
import com.kkuil.blackchat.domain.enums.error.UserErrorEnum;
import com.kkuil.blackchat.service.GroupMemberService;
import com.kkuil.blackchat.service.RoomService;
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

    @Resource
    private RoomService roomService;

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
     * @param uid            申请人ID
     * @param createGroupReq CreateGroupReq
     * @return 是否创建成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createGroup(Long uid, CreateGroupReq createGroupReq) {
        List<Long> uidList = createGroupReq.getUidList();

        String msg = createGroupReq.getMsg();

        // 1. 判断用户是否存在
        Boolean isExistUsers = userCache.isExistUsers(uidList);
        AssertUtil.isTrue(isExistUsers, UserErrorEnum.USER_NOT_EXIST.getMsg());

        // 2. 判断是否已经是好友关系
        List<UserSearchRespVO> friend = roomFriendDao.isFriend(uid, uidList);
        friend.forEach(user -> {
            AssertUtil.isTrue(user.getIsFriend(), UserErrorEnum.NOT_FRIEND.getMsg());
        });

        // 3. 检查人数是否达到上限
        int uidCount = createGroupReq.getUidList().size();
        AssertUtil.isTrue(uidCount + 1 < GroupConst.MAX_COUNT_PER_GROUP, ChatErrorEnum.MAX_COUNT_PER_GROUP_LIMIT.getMsg());

        // 4. 判断是否已经达到建群上限
        Long count = groupMemberDao.getCreateGroupCountByUid(uid);
        AssertUtil.isTrue(count < GroupConst.MAX_CREATE_GROUP_COUNT, ChatErrorEnum.CREATE_GROUP_MAX_COUNT.getMsg());

        // 5. 提交申请
        Boolean isAddFriend = userApplyDao.applyCreateGroup(uid, uidList, msg);
        AssertUtil.isTrue(isAddFriend, UserErrorEnum.COMMIT_APPLY_FAIL.getMsg());

        // 6. 创建群聊
        roomService.createGroup(uid, createGroupReq.getGroupName());

        return UserMessageEnum.COMMIT_APPLY_SUCESS.getMsg();
    }

    /**
     * 同意加群
     *
     * @param userApply 用户申请记录
     */
    @Override
    public void agreeAddGroup(UserApply userApply) {

    }

}
