package com.kkuil.blackchat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.Room;
import com.kkuil.blackchat.service.IRoomService;
import com.kkuil.blackchat.mapper.RoomMapper;
import org.springframework.stereotype.Service;

/**
* @author 小K
* @description 针对表【room(会话表)】的数据库操作Service实现
* @createDate 2023-09-27 11:55:52
*/
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room>
    implements IRoomService {

}
