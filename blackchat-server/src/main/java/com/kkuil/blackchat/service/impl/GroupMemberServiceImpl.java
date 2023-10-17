package com.kkuil.blackchat.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.kkuil.blackchat.cache.RoomGroupCache;
import com.kkuil.blackchat.cache.UserCache;
import com.kkuil.blackchat.constant.GroupConst;
import com.kkuil.blackchat.core.contact.domain.vo.request.AddAdminReq;
import com.kkuil.blackchat.core.contact.domain.vo.request.InvitAddGroupReq;
import com.kkuil.blackchat.core.user.domain.vo.request.CreateGroupReq;
import com.kkuil.blackchat.core.user.domain.vo.response.UserSearchRespVO;
import com.kkuil.blackchat.dao.*;
import com.kkuil.blackchat.domain.bo.room.GroupBaseInfo;
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

import java.util.*;

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

    @Resource
    private RoomGroupCache roomGroupCache;

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
        Boolean isGroupShip = groupMemberDao.isGroupShip(groupId, Collections.singletonList(uid));
        AssertUtil.isTrue(isGroupShip, ChatErrorEnum.NOT_IN_GROUP.getMsg());

        Room room = roomDao.getById(groupId);
        if (room.isHotRoom()) {
            return ChatErrorEnum.NOT_ALLOWED_TO_EXIT_ALL_GROUP.getErrorMsg();
        }

        // 判断当前退出的人是否是群主
        Boolean isMaster = groupMemberDao.isMaster(groupId, uid);
        if (isMaster) {
            // 删房间
            roomDao.deleteById(groupId);
            // 删会话
            contactDao.deleteByRoomId(groupId);
            // 删消息
            messageDao.deleteByRoomId(groupId);
            // 删成员
            groupMemberDao.deleteByRoomId(groupId);
            return ChatMessageEnum.DEL_GROUP_SUCCESS.getMsg();
        } else {
            // 删除成员
            Boolean isExitGroup = groupMemberDao.exitGroup(groupId, uid);
            AssertUtil.isTrue(isExitGroup, CommonErrorEnum.SYSTEM_ERROR.getMsg());

            // 删除会话记录
            Boolean isDelContact = contactDao.delContact(groupId, uid);
            AssertUtil.isTrue(isDelContact, CommonErrorEnum.SYSTEM_ERROR.getMsg());

            // 删除消息记录
            // TODO 这个地方想实现就打开注释
            // Boolean isDelMsg = messageDao.delRecordBatch(groupId, uid);
            // AssertUtil.isTrue(isDelMsg, CommonErrorEnum.SYSTEM_ERROR.getMsg());
            return ChatMessageEnum.EXIT_GROUP_SUCCESS.getMsg();
        }

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
        // 0. 判断群是否存在
        Long groupId = userApply.getExtraInfo().getGroupId();
        Room room = roomDao.getById(groupId);
        AssertUtil.isFalse(ObjectUtil.isNull(room), ChatErrorEnum.ROOM_NOT_EXIST.getMsg());

        // 1. 判断是否已经是群成员
        Boolean isGroupShip = groupMemberDao.isGroupShip(groupId, Collections.singletonList(userApply.getTargetId()));
        AssertUtil.isFalse(isGroupShip, ChatErrorEnum.EXIST_GROUP_MEMBER.getMsg());

        // 2. 判断是否达到群人数上限
        List<Long> uidList = groupMemberDao.getUidListByRoomId(groupId);
        AssertUtil.isTrue(uidList.size() <= GroupConst.MAX_COUNT_PER_GROUP - 1, ChatErrorEnum.MAX_COUNT_PER_GROUP_LIMIT.getMsg());

        // 3. 加群
        this.addGroup(groupId, Collections.singletonList(userApply.getTargetId()));
    }

    /**
     * 邀请加群
     *
     * @param uid              邀请人ID
     * @param invitAddGroupReq 邀请加群的请求信息
     * @return 是否邀请成功
     */
    @Override
    public Boolean inviteGroup(Long uid, InvitAddGroupReq invitAddGroupReq) {
        // -1. 判断邀请好友列表是否为空
        AssertUtil.isFalse(invitAddGroupReq.getUidList().size() <= 0, ChatErrorEnum.EMPTY_LIST.getMsg());

        // 0. 判断群是否存在
        Long groupId = invitAddGroupReq.getGroupId();
        GroupBaseInfo baseInfo = roomGroupCache.getBaseInfoById(groupId);
        AssertUtil.isFalse(ObjectUtil.isNull(baseInfo), ChatErrorEnum.ROOM_NOT_EXIST.getMsg());

        // 1. 判断该用户是否有权限邀请人加群
        Boolean hasPower = groupMemberDao.hasPowerForInviteGroup(groupId, uid);
        AssertUtil.isTrue(hasPower, ChatErrorEnum.EXIST_GROUP_MEMBER.getMsg());

        // 2. 判断是否达到群人数上限  TODO 在这里我们不用现群人数+传过来的uidList数量进行判断，是因为可能uidList中有已入群的uid
        List<Long> uidList = roomGroupCache.getGroupUidByRoomId(groupId);
        uidList.addAll(invitAddGroupReq.getUidList());
        Set<Long> uidListDistinct = new HashSet<>(uidList);
        AssertUtil.isTrue(uidListDistinct.size() <= GroupConst.MAX_COUNT_PER_GROUP, ChatErrorEnum.MAX_COUNT_PER_GROUP_LIMIT.getMsg());

        // 3. 发出加群邀请
        Boolean isInvited = userApplyDao.inviteGroup(uid, groupId, invitAddGroupReq.getUidList(), invitAddGroupReq.getMsg());
        AssertUtil.isTrue(isInvited, ChatErrorEnum.EXIST_GROUP_MEMBER.getMsg());
        return true;
    }

    /**
     * 添加管理
     *
     * @param uid         用户ID
     * @param addAdminReq 请求信息
     * @return 是否添加成功
     */
    @Override
    public Boolean addAdmin(Long uid, AddAdminReq addAdminReq) {
        Long groupId = addAdminReq.getGroupId();

        // 1. 判断权限身份
        Boolean isMaster = groupMemberDao.isMaster(groupId, uid);
        AssertUtil.isTrue(isMaster, ChatErrorEnum.NO_POWER_FOR_ADD_ADMIN.getMsg());

        // 2. 判断成员列表是否是群中成员
        List<Long> uidList = addAdminReq.getUidList();
        Boolean isGroupShip = groupMemberDao.isGroupShip(groupId, uidList);
        AssertUtil.isTrue(isGroupShip, ChatErrorEnum.NOT_ALLOWED_ADD_ADMIN_WITH_NOT_MEMBER.getMsg());

        // 3. 判断管理员uidList
        List<Long> adminUidList = groupMemberDao.getAdminCount(groupId);
        // TODO 这里是为了防止adminUidList为空，报不支持异常
        List<Long> newAdminUidList = new ArrayList<>(adminUidList);
        newAdminUidList.addAll(uidList);
        adminUidList = newAdminUidList;
        Set<Long> uidSet = new HashSet<>(adminUidList);
        AssertUtil.isTrue(uidSet.size() <= GroupConst.MAX_ADMIN_COUNT_PER_GROUP, ChatErrorEnum.MAX_ADMIN_COUNT_LIMIT.getMsg());

        // 4. 添加管理员
        groupMemberDao.setAdmin(groupId, uidList);

        return true;
    }

    /**
     * 加群
     *
     * @param groupId 群ID
     * @param uidList 需要入群的ID列表
     */
    private void addGroup(Long groupId, List<Long> uidList) {
        // 1. 加群
        groupMemberDao.addGroupMember(groupId, uidList);

        // 2. 创建会话
        contactDao.createContactBatch(groupId, uidList, new Date());
    }
}
