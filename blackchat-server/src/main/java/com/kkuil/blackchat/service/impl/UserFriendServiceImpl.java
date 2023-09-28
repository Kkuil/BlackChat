package com.kkuil.blackchat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.UserFriend;
import com.kkuil.blackchat.service.UserFriendService;
import com.kkuil.blackchat.mapper.UserFriendMapper;
import org.springframework.stereotype.Service;

/**
* @author 小K
* @description 针对表【user_friend(用户联系人表)】的数据库操作Service实现
* @createDate 2023-09-28 14:48:23
*/
@Service
public class UserFriendServiceImpl extends ServiceImpl<UserFriendMapper, UserFriend>
    implements UserFriendService {

}
