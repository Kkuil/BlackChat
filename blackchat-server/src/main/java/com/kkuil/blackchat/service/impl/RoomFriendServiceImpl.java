package com.kkuil.blackchat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.RoomFriend;
import com.kkuil.blackchat.service.RoomFriendService;
import com.kkuil.blackchat.mapper.RoomFriendMapper;
import org.springframework.stereotype.Service;

/**
* @author 小K
* @description 针对表【room_friend(单聊房间表)】的数据库操作Service实现
* @createDate 2023-09-28 14:47:49
*/
@Service
public class RoomFriendServiceImpl extends ServiceImpl<RoomFriendMapper, RoomFriend>
    implements RoomFriendService {

}
