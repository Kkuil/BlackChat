package com.kkuil.blackchat.cache;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kkuil.blackchat.constant.RedisKeyConst;
import com.kkuil.blackchat.dao.GroupMemberDAO;
import com.kkuil.blackchat.domain.entity.GroupMember;
import com.kkuil.blackchat.utils.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/9/27 11:26
 * @Description 群缓存
 */
@Component
public class GroupCache {

    @Resource
    private GroupMemberDAO groupMemberDao;

    /**
     * 通过房间ID获取群成员id
     *
     * @param roomId 房间ID
     */
    public List getGroupUidByRoomId(Long roomId) {
        String key = RedisKeyConst.getKey(RedisKeyConst.GROUP_UID_STRING, roomId);
        List list2 = RedisUtil.get(key, List.class);

        if (list2 == null) {
            LambdaQueryWrapper<GroupMember> wrapper = new QueryWrapper<GroupMember>().lambda().eq(GroupMember::getRoomId, roomId);
            List<GroupMember> list = groupMemberDao.list(wrapper);
            List<Long> list1 = list.stream().map(GroupMember::getUid).toList();

            RedisUtil.set(key, list1);
            return list1;
        } else {
            return list2;
        }
    }
}
