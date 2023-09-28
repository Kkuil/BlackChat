package com.kkuil.blackchat.dao;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.GroupMember;
import com.kkuil.blackchat.domain.entity.RoomGroup;
import com.kkuil.blackchat.domain.enums.error.ChatErrorEnum;
import com.kkuil.blackchat.mapper.RoomGroupMapper;
import com.kkuil.blackchat.utils.AssertUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/9/28 16:20
 * @Description 群访问层
 */
@Service
public class RoomGroupDAO extends ServiceImpl<RoomGroupMapper, RoomGroup> {

    @Resource
    private GroupMemberDAO groupMemberDao;

    /**
     * 群聊中是否有该用户
     *
     * @param roomId 房间ID
     * @param uid    用户ID
     * @return 群聊中是否存在该用户
     */
    public Boolean hasUser(Long roomId, Long uid) {
        // 1. 根据房间ID查询相应的群主ID
        LambdaQueryChainWrapper<RoomGroup> wrapper = lambdaQuery().eq(RoomGroup::getRoomId, roomId);
        RoomGroup roomGroup = this.getOne(wrapper);
        AssertUtil.isNotEmpty(roomGroup, ChatErrorEnum.ROOM_NOT_EXIST.getMsg());
        Long groupId = roomGroup.getId();
        // 2. 根据群主ID，获取群成员信息
        GroupMember groupMember = groupMemberDao.getMemberByGroupIdUid(groupId, uid);
        AssertUtil.isNotEmpty(groupMember, ChatErrorEnum.NOT_IN_GROUP.getMsg());
        return true;
    }

    /**
     * 判断是否是群友
     *
     * @param roomId 房间ID
     * @param uids   一群用户
     * @return 是否是群友
     */
    public Boolean isGroupShip(Long roomId, Long... uids) {
        // 1. 根据房间ID查询相应的群主ID
        LambdaQueryChainWrapper<RoomGroup> wrapper = lambdaQuery().eq(RoomGroup::getRoomId, roomId);
        RoomGroup roomGroup = this.getOne(wrapper);
        AssertUtil.isNotEmpty(roomGroup, ChatErrorEnum.ROOM_NOT_EXIST.getMsg());
        Long groupId = roomGroup.getId();
        // 2. 根据群主ID，获取群成员信息
        List<Long> list = Arrays.stream(uids).filter(uid -> {
            GroupMember groupMember = groupMemberDao.getMemberByGroupIdUid(groupId, uid);
            return groupMember == null;
        }).toList();
        AssertUtil.isFalse(list.size() > 0, ChatErrorEnum.NOT_IN_GROUP.getMsg());
        return true;
    }

    /**
     * 通过房间ID获取群信息
     *
     * @param roomId 房间ID
     * @return 群信息
     */
    public RoomGroup getByRoomId(Long roomId) {
        LambdaQueryChainWrapper<RoomGroup> wrapper = lambdaQuery().eq(RoomGroup::getRoomId, roomId);
        return this.getOne(wrapper);
    }
}
