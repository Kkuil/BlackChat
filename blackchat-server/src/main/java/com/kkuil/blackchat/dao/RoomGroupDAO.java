package com.kkuil.blackchat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.RoomGroup;
import com.kkuil.blackchat.mapper.RoomGroupMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

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
     * 通过房间ID获取群信息
     *
     * @param roomId 房间ID
     * @return 群信息
     */
    public RoomGroup getByRoomId(Long roomId) {
        return lambdaQuery().eq(RoomGroup::getRoomId, roomId).one();
    }
}
