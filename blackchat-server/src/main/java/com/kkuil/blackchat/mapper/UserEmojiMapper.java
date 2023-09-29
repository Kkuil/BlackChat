package com.kkuil.blackchat.mapper;

import com.kkuil.blackchat.domain.entity.UserEmoji;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Kkuil
 * @Date 2023/9/29 13:10
 * @Description 针对表【user_emoji(用户表情包)】的数据库操作Mapper
 */
@Mapper
public interface UserEmojiMapper extends BaseMapper<UserEmoji> {

}
