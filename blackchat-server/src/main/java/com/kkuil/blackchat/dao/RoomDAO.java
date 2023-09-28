package com.kkuil.blackchat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.Room;
import com.kkuil.blackchat.mapper.RoomMapper;
import org.springframework.stereotype.Service;

/**
 * @Author Kkuil
 * @Date 2023/9/28 12:48
 * @Description 房间访问层
 */
@Service
public class RoomDAO extends ServiceImpl<RoomMapper, Room> {
}
