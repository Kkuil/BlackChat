package com.kkuil.blackchat.mapper;

import com.kkuil.blackchat.domain.entity.RoomFriend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Kkuil
 * @Date 2023/9/29 13:09
 * @Description 针对表【room_friend(单聊房间表)】的数据库操作Mapper
 */
@Mapper
public interface RoomFriendMapper extends BaseMapper<RoomFriend> {

}
