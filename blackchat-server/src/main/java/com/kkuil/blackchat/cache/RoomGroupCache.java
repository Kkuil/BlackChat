package com.kkuil.blackchat.cache;

import cn.hutool.core.bean.BeanUtil;
import com.kkuil.blackchat.dao.RoomGroupDAO;
import com.kkuil.blackchat.domain.bo.room.GroupBaseInfo;
import com.kkuil.blackchat.domain.entity.RoomGroup;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @Author Kkuil
 * @Date 2023/10/5 20:12
 * @Description 群缓存
 */
@Component
public class RoomGroupCache {

    @Resource
    private RoomGroupDAO roomGroupDao;

    /**
     * 通过房间ID获取群信息
     *
     * @param roomId 房间ID
     * @return 群信息
     */
    public GroupBaseInfo getBaseInfoById(Long roomId) {
        RoomGroup roomGroup = roomGroupDao.getByRoomId(roomId);
        GroupBaseInfo groupBaseInfo = BeanUtil.toBean(roomGroup, GroupBaseInfo.class);
        return groupBaseInfo;
    }

}
