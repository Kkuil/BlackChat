package com.kkuil.blackchat.mapper;

import com.kkuil.blackchat.domain.entity.Room;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Kkuil
 * @Date 2023/9/28 14:50
 * @Description 针对表【room(房间表)】的数据库操作Mapper
 */
@Mapper
public interface RoomMapper extends BaseMapper<Room> {

}
