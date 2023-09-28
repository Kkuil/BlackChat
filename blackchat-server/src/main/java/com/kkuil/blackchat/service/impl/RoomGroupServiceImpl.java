package com.kkuil.blackchat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.RoomGroup;
import com.kkuil.blackchat.service.RoomGroupService;
import com.kkuil.blackchat.mapper.RoomGroupMapper;
import org.springframework.stereotype.Service;

/**
* @author 小K
* @description 针对表【room_group(群聊房间表)】的数据库操作Service实现
* @createDate 2023-09-28 14:47:59
*/
@Service
public class RoomGroupServiceImpl extends ServiceImpl<RoomGroupMapper, RoomGroup>
    implements RoomGroupService {

}
