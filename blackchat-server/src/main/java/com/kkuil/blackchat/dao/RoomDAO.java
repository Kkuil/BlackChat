package com.kkuil.blackchat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.Room;
import com.kkuil.blackchat.mapper.RoomMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author Kkuil
 * @Date 2023/9/28 12:48
 * @Description 房间访问层
 */
@Service
public class RoomDAO extends ServiceImpl<RoomMapper, Room> {

    /**
     * 更新房间的最新消息
     *
     * @param roomId     房间号
     * @param activeTime 消息最新时间
     * @param lastMsgId  最新消息ID
     */
    public void updateRoomNewestMsg(Long roomId, Date activeTime, Long lastMsgId) {
        this.lambdaUpdate()
                .eq(Room::getId, roomId)
                .set(Room::getActiveTime, activeTime)
                .set(Room::getLastMsgId, lastMsgId);
    }
}
