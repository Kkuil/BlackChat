package com.kkuil.blackchat.dao;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.GroupMember;
import com.kkuil.blackchat.domain.entity.Room;
import com.kkuil.blackchat.domain.entity.RoomGroup;
import com.kkuil.blackchat.domain.enums.error.ChatErrorEnum;
import com.kkuil.blackchat.mapper.GroupMemberMapper;
import com.kkuil.blackchat.service.RoomService;
import com.kkuil.blackchat.utils.AssertUtil;
import com.kkuil.blackchat.web.chat.domain.enums.RoomTypeEnum;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        Integer hotFlag = room.getHotFlag();
        if (hotFlag == 1) {
            // 2.1.1 大群聊
            return userRoleDao.hasAuthorities(uid);
        } else {
            // 2.1.2 普通群聊（是否是群主或者群管理员）
            return this.hasAuthorities(roomId, uid, authorities);
        }
    }

    /**
     * 群聊中是否有该用户
     *
     * @param roomId 房间ID
     * @param uid    用户ID
     * @return 群聊中是否存在该用户
     */
    public Boolean hasUser(Long roomId, Long uid) {
        LambdaQueryChainWrapper<GroupMember> wrapper = lambdaQuery().eq(GroupMember::getRoomId, roomId).eq(GroupMember::getUid, uid);
        GroupMember groupMember = this.getOne(wrapper);
        return groupMember != null;
    }


    /**
     * 判断是否是群友
     *
     * @param roomId 房间ID
     * @param uids   一群用户
     * @return 是否是群友
     */
    public Boolean isGroupShip(Long roomId, Long... uids) {
        // 1. 通过ID查询房间内的所有成员ID
        LambdaQueryChainWrapper<GroupMember> wrapper = lambdaQuery().eq(GroupMember::getRoomId, roomId);
        wrapper.select(GroupMember::getUid);
        List<GroupMember> list = this.list(wrapper);
        // 2. 判断每个成员是否在这个集合中
        List<Long> uidListInRoom = list.stream().map(GroupMember::getUid).toList();
        for (Long uid : uids) {
            boolean contains = uidListInRoom.contains(uid);
            AssertUtil.isTrue(contains, ChatErrorEnum.NOT_IN_GROUP.getMsg());
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
}
