package com.kkuil.blackchat.cache;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.kkuil.blackchat.constant.RedisKeyConst;
import com.kkuil.blackchat.dao.GroupMemberDAO;
import com.kkuil.blackchat.dao.RoomGroupDAO;
import com.kkuil.blackchat.domain.bo.room.GroupBaseInfo;
import com.kkuil.blackchat.domain.entity.GroupMember;
import com.kkuil.blackchat.domain.entity.RoomGroup;
import com.kkuil.blackchat.utils.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/10/5 20:12
 * @Description 群缓存
 */
@Component
public class RoomGroupCache {

    @Resource
    private RoomGroupDAO roomGroupDao;

    @Resource
    private GroupMemberDAO groupMemberDao;

    /**
     * 通过房间ID获取群信息
     *
     * @param roomId 房间ID
     * @return 群信息
     */
    public GroupBaseInfo getBaseInfoById(Long roomId) {
        String key = RedisKeyConst.getKey(RedisKeyConst.GROUP_INFO_STRING, roomId);
        GroupBaseInfo baseInfo = RedisUtil.get(key, GroupBaseInfo.class);

        if (ObjectUtil.isNotNull(baseInfo)) {
            return baseInfo;
        }

        // 查询数据库
        RoomGroup roomGroup = roomGroupDao.getByRoomId(roomId);
        GroupBaseInfo groupBaseInfo = BeanUtil.toBean(roomGroup, GroupBaseInfo.class);
        groupBaseInfo.setMemberList(groupMemberDao.getUidListByRoomId(roomId));
        // 更新缓存
        RedisUtil.set(key, groupBaseInfo);
        return groupBaseInfo;
    }

    /**
     * 通过房间ID获取群成员id
     *
     * @param roomId 房间ID
     */
    public List<Long> getGroupUidByRoomId(Long roomId) {
        String key = RedisKeyConst.getKey(RedisKeyConst.GROUP_INFO_STRING, roomId);
        GroupBaseInfo groupBaseInfo = RedisUtil.get(key, GroupBaseInfo.class);

        if (ObjectUtil.isNotNull(groupBaseInfo)) {
            return groupBaseInfo.getMemberList();
        }
        // 更新数据库
        return this.getBaseInfoById(roomId).getMemberList();
    }

}
