package com.kkuil.blackchat.mapper;

import com.kkuil.blackchat.domain.entity.Room;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 小K
* @description 针对表【room(会话表)】的数据库操作Mapper
* @createDate 2023-09-27 11:55:52
* @Entity com.kkuil.blackchat.domain.entity.Room
*/
@Mapper
public interface RoomMapper extends BaseMapper<Room> {

}
