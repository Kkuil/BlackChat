package com.kkuil.blackchat.dao;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.cache.RoomGroupCache;
import com.kkuil.blackchat.constant.RedisKeyConst;
import com.kkuil.blackchat.core.contact.domain.bo.GroupMemberBaseInfo;
import com.kkuil.blackchat.core.contact.domain.enums.GroupRoleEnum;
import com.kkuil.blackchat.domain.bo.room.GroupBaseInfo;
import com.kkuil.blackchat.domain.entity.GroupMember;
import com.kkuil.blackchat.domain.entity.Room;
import com.kkuil.blackchat.domain.enums.error.ChatErrorEnum;
import com.kkuil.blackchat.domain.enums.error.CommonErrorEnum;
import com.kkuil.blackchat.mapper.GroupMemberMapper;
import com.kkuil.blackchat.service.RoomService;
import com.kkuil.blackchat.utils.AssertUtil;
import com.kkuil.blackchat.utils.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author Kkuil
 * @Date 2023/9/28 15:02
 * @Description 群成员访问层
 */
@Service
public class GroupMemberDAO extends ServiceImpl<GroupMemberMapper, GroupMember> {

    @Resource
    private RoomDAO roomDao;

    @Resource
    @Lazy
    private RoomService roomService;

    @Resource
    private UserRoleDAO userRoleDao;

    @Resource
    @Lazy
    private RoomGroupCache roomGroupCache;

    /**
     * 判断某名用户在某个房间内是否有指定权限
     *
     * @param roomId      房间ID
     * @param uid         用户ID
     * @param authorities 权限列表
     * @return 是否权限
     */
    public Boolean hasAuthority(Long roomId, Long uid, List<Integer> authorities) {
        // 1. 判断是否在房间内
        Boolean isSameRoom = roomService.checkRoomMembership(roomId, uid);
        AssertUtil.isTrue(isSameRoom, ChatErrorEnum.NOT_IN_GROUP.getMsg());
        // 2. 判断权限
        // 2.1 判断是否是大群聊
        Room room = roomDao.getById(roomId);
        if (room.isHotRoom()) {
            // 2.1.1 大群聊
            return userRoleDao.hasAuthorities(uid);
        } else {
            // 2.1.2 普通群聊（是否是群主或者群管理员）
            return this.hasAuthorities(roomId, uid, authorities);
        }
    }

    /**
     * 判断是否是群友
     *
     * @param roomId 房间ID
     * @param uids   一群用户
     * @return 是否是群友
     */
    public Boolean isGroupShip(Long roomId, List<Long> uids) {
        // 1. 通过ID查询房间内的所有成员ID
        List<Long> uidList = roomGroupCache.getGroupUidByRoomId(roomId);
        // 2. 判断每个成员是否在这个集合中
        for (Long uid : uids) {
            boolean isContains = uidList.contains(uid);
            if (!isContains) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断群聊中的用户是否有指定权限中的一种即可
     *
     * @param roomId      房间ID
     * @param uid         用户ID
     * @param authorities 权限列表
     * @return 是否有权限
     */
    public Boolean hasAuthorities(Long roomId, Long uid, List<Integer> authorities) {
        // 1. 判断权限列表是否为空
        boolean empty = CollectionUtil.isEmpty(authorities);
        if (empty) {
            return true;
        }
        // 2. 判断用户是否拥有权限
        LambdaQueryChainWrapper<GroupMember> wrapper = this.lambdaQuery().eq(GroupMember::getRoomId, roomId).eq(GroupMember::getUid, uid).select(GroupMember::getRole);
        GroupMember groupMember = this.getOne(wrapper);
        Integer role = groupMember.getRole();
        return authorities.contains(role);
    }

    /**
     * 通过房间号获取房间成员uid列表
     *
     * @param roomId 房间ID
     * @return 成员列表
     */
    public List<Long> getUidListByRoomId(Long roomId) {
        return roomGroupCache.getGroupUidByRoomId(roomId);
    }

    /**
     * 退出群聊
     *
     * @param groupId 群聊ID
     * @param uid     用户ID
     * @return 是否退出
     */
    public Boolean exitGroup(Long groupId, Long uid) {
        // 删库
        QueryWrapper<GroupMember> wrapper = new QueryWrapper<GroupMember>()
                .eq("room_id", groupId)
                .eq("uid", uid);

        // 更新缓存
        String key = RedisKeyConst.getKey(RedisKeyConst.GROUP_INFO_STRING, groupId);
        GroupBaseInfo groupBaseInfo = RedisUtil.get(key, GroupBaseInfo.class);
        List<Long> list = groupBaseInfo.getMemberList().stream().filter(id -> BooleanUtil.isFalse(id.equals(uid))).toList();
        groupBaseInfo.setMemberList(list);
        RedisUtil.set(key, groupBaseInfo);
        return this.remove(wrapper);
    }

    /**
     * 获取用户建群数量
     *
     * @param uid 用户ID
     * @return 建群数量
     */
    public Long getCreateGroupCountByUid(Long uid) {
        return this.lambdaQuery()
                .eq(GroupMember::getUid, uid)
                .eq(GroupMember::getRole, GroupRoleEnum.MASTER.getId())
                .count();
    }

    /**
     * 将用户加入群
     *
     * @param roomId 房间ID
     * @param list   成员列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void createGroup(Long roomId, List<GroupMemberBaseInfo> list) {
        List<GroupMember> saveBatchList = new ArrayList<>();
        list.forEach(groupMemberBaseInfo -> {
            GroupMember groupMember = new GroupMember();
            groupMember.setRoomId(roomId);
            groupMember.setUid(groupMemberBaseInfo.getUid());
            groupMember.setRole(groupMemberBaseInfo.getRole());
            saveBatchList.add(groupMember);
        });
        // 注意：这里得去更新缓存中的成员列表
        this.addGroupMember(roomId, saveBatchList.stream().map(GroupMember::getUid).toList());
        this.saveBatch(saveBatchList);
    }

    /**
     * 该用户是否有邀请人加群的权限
     *
     * @param groupId 群ID
     * @param uid     邀请人ID
     * @return 是否有权限
     */
    public Boolean hasPowerForInviteGroup(Long groupId, Long uid) {
        return this.lambdaQuery()
                .eq(GroupMember::getRoomId, groupId)
                .eq(GroupMember::getUid, uid)
                .eq(GroupMember::getRole, GroupRoleEnum.MASTER.getId())
                .or()
                .eq(GroupMember::getRoomId, groupId)
                .eq(GroupMember::getUid, uid)
                .eq(GroupMember::getRole, GroupRoleEnum.ADMIN.getId())
                .exists();
    }

    /**
     * 加群
     *
     * @param groupId 群ID
     * @param uidList 用户列表ID
     */
    public void addGroupMember(Long groupId, List<Long> uidList) {
        List<GroupMember> saveBatchList = new ArrayList<>();
        uidList.forEach(uid -> {
            GroupMember groupMember = new GroupMember();
            groupMember.setRoomId(groupId);
            groupMember.setUid(uid);
            groupMember.setRole(GroupRoleEnum.MEMBER.getId());
            saveBatchList.add(groupMember);
        });
        // 更新缓存
        String roomKey = String.format(RedisKeyConst.GROUP_INFO_STRING, groupId);
        String key = RedisKeyConst.getKey(roomKey);
        GroupBaseInfo groupBaseInfo = RedisUtil.get(key, GroupBaseInfo.class);
        AssertUtil.isNotEmpty(groupBaseInfo, CommonErrorEnum.SYSTEM_ERROR.getMsg());
        // 合并群成员并去重
        groupBaseInfo.getMemberList().addAll(uidList);
        Set<Long> uidSet = new HashSet<>(groupBaseInfo.getMemberList());
        RedisUtil.set(key, uidSet.stream().toList());
        this.saveBatch(saveBatchList);
    }

    /**
     * 判断该用户是否是群主
     *
     * @param groupId 房间ID
     * @param uid     用户ID
     * @return 是否是群主
     */
    public Boolean isMaster(Long groupId, Long uid) {
        return this.lambdaQuery()
                .eq(GroupMember::getRoomId, groupId)
                .eq(GroupMember::getUid, uid)
                .eq(GroupMember::getRole, GroupRoleEnum.MASTER.getId())
                .exists();
    }

    /**
     * 通过房间ID删除群成员
     *
     * @param roomId 房间ID
     */
    public void deleteByRoomId(Long roomId) {
        LambdaQueryWrapper<GroupMember> wrapper = new QueryWrapper<GroupMember>()
                .lambda()
                .eq(GroupMember::getRoomId, roomId);
        this.remove(wrapper);
    }

    /**
     * 获取用户在群中的角色ID
     *
     * @param roomId 房间ID
     * @param uid    用户ID
     * @return 角色ID
     */
    public Integer getRoleId(Long roomId, Long uid) {
        return this.lambdaQuery()
                .eq(GroupMember::getRoomId, roomId)
                .eq(GroupMember::getUid, uid)
                .select(GroupMember::getRole)
                .one()
                .getRole();
    }

    /**
     * 获取管理员数量
     *
     * @param groupId 群ID
     * @return 数量
     */
    public List<Long> getAdminCount(Long groupId) {
        return this.lambdaQuery()
                .eq(GroupMember::getRoomId, groupId)
                .eq(GroupMember::getRole, GroupRoleEnum.ADMIN.getId())
                .list()
                .stream()
                .map(GroupMember::getUid)
                .toList();
    }

    /**
     * 添加管理员
     *
     * @param groupId 群ID
     * @param uidList 需要添加用户的ID列表
     */
    public void setAdmin(Long groupId, List<Long> uidList) {
        Wrapper<GroupMember> wrapper = new UpdateWrapper<GroupMember>()
                .lambda()
                .eq(GroupMember::getRoomId, groupId)
                .in(GroupMember::getUid, uidList)
                .set(GroupMember::getRole, GroupRoleEnum.ADMIN.getId());
        this.update(wrapper);
    }
}
