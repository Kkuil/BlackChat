package com.kkuil.blackchat.service.impl;

import com.kkuil.blackchat.cache.RoomCache;
import com.kkuil.blackchat.core.contact.domain.enums.GroupRoleEnum;
import com.kkuil.blackchat.core.contact.domain.vo.request.UpdateGroupInfoReq;
import com.kkuil.blackchat.dao.GroupMemberDAO;
import com.kkuil.blackchat.dao.RoomGroupDAO;
import com.kkuil.blackchat.domain.bo.room.RoomBaseInfo;
import com.kkuil.blackchat.domain.enums.error.ChatErrorEnum;
import com.kkuil.blackchat.service.RoomGroupService;
import com.kkuil.blackchat.utils.AssertUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @Author Kkuil
 * @Date 2023/10/18 15:55
 * @Description 针对表【room_group(群聊房间表)】的数据库操作Service实现
 */
@Service
public class RoomGroupServiceImpl implements RoomGroupService {

    @Resource
    private GroupMemberDAO groupMemberDao;

    @Resource
    private RoomGroupDAO roomGroupDao;

    @Resource
    private RoomCache roomCache;

    /**
     * 更改群信息
     *
     * @param uid                用户ID
     * @param updateGroupInfoReq 请求信息
     * @return 是否更改成功
     */
    @Override
    public Boolean updateGroupInfo(Long uid, UpdateGroupInfoReq updateGroupInfoReq) {
        Long groupId = updateGroupInfoReq.getGroupId();
        String groupName = updateGroupInfoReq.getGroupName();
        AssertUtil.isTrue(groupName.length() >= 3, ChatErrorEnum.NOT_ALLOWED_LT_THREE_CHAR_FOR_GROUP_NAME.getMsg());
        AssertUtil.isTrue(groupName.length() <= 10, ChatErrorEnum.NOT_ALLOWED_GT_TEN_CHAR_FOR_GROUP_NAME.getMsg());

        // 0. 判断是否是大群聊
        RoomBaseInfo roomBaseInfo = roomCache.getRoomBaseInfoById(updateGroupInfoReq.getGroupId());
        AssertUtil.isFalse(roomBaseInfo.getHotFlag() == 1, ChatErrorEnum.NOT_ALLOWED_UPDATE_GROUP_NAME_FOR_HOT_GROUP.getMsg());

        // 1. 判断该用户是否有权限更改群名
        Boolean isMaster = groupMemberDao.hasAuthority(groupId, uid, Arrays.asList(GroupRoleEnum.MASTER, GroupRoleEnum.ADMIN));
        AssertUtil.isTrue(isMaster, ChatErrorEnum.NO_POWER_FOR_UPDATE_GROUP_NAME_ADMIN.getMsg());

        // 2. 更改信息
        roomGroupDao.updateGroupInfo(groupId, updateGroupInfoReq);

        return true;
    }
}
