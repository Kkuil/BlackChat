package com.kkuil.blackchat.dao;

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

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/9/28 15:02
 * @Description 群成员访问层
 */
public class GroupMemberDAO extends ServiceImpl<GroupMemberMapper, GroupMember> {

    @Resource
    private RoomDAO roomDao;

    @Resource
    private RoomService roomService;

    @Resource
    private UserRoleDAO userRoleDao;

    @Resource
    private RoomGroupDAO roomGroupDao;

    /**
     * 判断某名用户在某个房间内是否有指定权限
     *
     * @param roomId     房间ID
     * @param uid        用户ID
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
        }
    }

    /**
     * 根据群主ID和用户ID获取成员信息
     *
     * @param groupId 群主ID
     * @param uid     用户ID
     * @return 群成员信息
     */
    public GroupMember getMemberByGroupIdUid(Long groupId, Long uid) {
        LambdaQueryChainWrapper<GroupMember> wrapper = lambdaQuery()
                .eq(GroupMember::getGroupId, groupId)
                .eq(GroupMember::getUid, uid);
        GroupMember groupMember = this.getOne(wrapper);
        return groupMember;
    }

    /**
     * 通过群主ID获取群信息
     *
     * @param roomId 房间ID
     * @return 群信息
     */
    public RoomGroup getByRoomId(Long roomId) {
        LambdaQueryChainWrapper<RoomGroup> wrapper = lambdaQuery().eq(RoomGroup::getRoomId, roomId);
        return this.getOne(wrapper);
    }
}
