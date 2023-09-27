package com.kkuil.blackchat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.UserEmoji;
import com.kkuil.blackchat.service.IUserEmojiService;
import com.kkuil.blackchat.mapper.UserEmojiMapper;
import org.springframework.stereotype.Service;

/**
* @author 小K
* @description 针对表【user_emoji(用户表情包)】的数据库操作Service实现
* @createDate 2023-09-27 11:56:07
*/
@Service
public class UserEmojiServiceImpl extends ServiceImpl<UserEmojiMapper, UserEmoji>
    implements IUserEmojiService {

}
