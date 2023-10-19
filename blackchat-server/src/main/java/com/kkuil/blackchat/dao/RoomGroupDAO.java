package com.kkuil.blackchat.dao;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.constant.RedisKeyConst;
import com.kkuil.blackchat.core.contact.domain.vo.request.UpdateGroupInfoReq;
import com.kkuil.blackchat.domain.bo.room.GroupBaseInfo;
import com.kkuil.blackchat.domain.entity.RoomGroup;
import com.kkuil.blackchat.domain.enums.error.CommonErrorEnum;
import com.kkuil.blackchat.mapper.RoomGroupMapper;
import com.kkuil.blackchat.utils.AssertUtil;
import com.kkuil.blackchat.utils.RedisUtil;
import org.springframework.stereotype.Service;

/**
 * @Author Kkuil
 * @Date 2023/9/28 16:20
 * @Description 群访问层
 */
@Service
public class RoomGroupDAO extends ServiceImpl<RoomGroupMapper, RoomGroup> {

    /**
     * 通过房间ID获取群信息
     *
     * @param roomId 房间ID
     * @return 群信息
     */
    public RoomGroup getByRoomId(Long roomId) {
        return lambdaQuery().eq(RoomGroup::getRoomId, roomId).one();
    }

    /**
     * 创建群
     *
     * @param roomId      房间ID
     * @param groupName   房间名
     * @param groupAvatar 房间头像
     */
    public void createGroup(Long roomId, String groupName, String groupAvatar) {
        RoomGroup roomGroup = new RoomGroup();
        roomGroup.setRoomId(roomId);
        roomGroup.setName(groupName);
        roomGroup.setAvatar(groupAvatar);
        this.save(roomGroup);
    }

    /**
     * 更新群名
     *
     * @param groupId   群ID
     * @param groupName 群名
     */
    public void updateGroupInfo(Long groupId, UpdateGroupInfoReq groupInfo) {
        LambdaUpdateWrapper<RoomGroup> wrapper = new UpdateWrapper<RoomGroup>()
                .lambda()
                .eq(RoomGroup::getRoomId, groupId)
                .set(ObjectUtil.isNotNull(groupInfo.getGroupName()), RoomGroup::getName, groupInfo.getGroupName())
                .set(ObjectUtil.isNotNull(groupInfo.getGroupAvatar()), RoomGroup::getAvatar, groupInfo.getGroupAvatar());
        // 更新数据库
        this.update(wrapper);

        // 更新缓存
        String key = RedisKeyConst.getKey(RedisKeyConst.GROUP_INFO_STRING, groupId);
        GroupBaseInfo groupBaseInfo = RedisUtil.get(key, GroupBaseInfo.class);
        AssertUtil.isNotEmpty(groupBaseInfo, CommonErrorEnum.SYSTEM_ERROR.getMsg());

        // 更改信息
        if (ObjectUtil.isNotNull(groupInfo.getGroupName())) {
            groupBaseInfo.setName(groupInfo.getGroupName());
        }
        if (ObjectUtil.isNotNull(groupInfo.getGroupAvatar())) {
            groupBaseInfo.setAvatar(groupInfo.getGroupAvatar());
        }
        RedisUtil.set(key, groupBaseInfo);
    }
}
