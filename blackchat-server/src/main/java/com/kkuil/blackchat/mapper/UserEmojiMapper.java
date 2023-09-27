package com.kkuil.blackchat.mapper;

import com.kkuil.blackchat.domain.entity.UserEmoji;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 小K
* @description 针对表【user_emoji(用户表情包)】的数据库操作Mapper
* @createDate 2023-09-27 11:56:07
* @Entity com.kkuil.blackchat.domain.entity.UserEmoji
*/
@Mapper
public interface UserEmojiMapper extends BaseMapper<UserEmoji> {

}
