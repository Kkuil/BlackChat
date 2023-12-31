package com.kkuil.blackchat.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.core.chat.domain.enums.HotFlagEnum;
import com.kkuil.blackchat.core.chat.domain.enums.RoomTypeEnum;
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
        LambdaUpdateWrapper<Room> updateWrapper = new UpdateWrapper<Room>()
                .lambda()
                .eq(Room::getId, roomId)
                .set(Room::getActiveTime, activeTime)
                .set(Room::getLastMsgId, lastMsgId);
        this.update(updateWrapper);
    }

    /**
     * 删除房间
     *
     * @param id 房间ID
     */
    public void deleteById(Long id) {
        LambdaQueryWrapper<Room> wrapper = new QueryWrapper<Room>().lambda()
                .eq(Room::getId, id);
        this.remove(wrapper);
    }

    /**
     * 创建房间
     *
     * @param roomTypeEnum 房间类型
     * @return room 房间
     */
    public Room createRoom(RoomTypeEnum roomTypeEnum) {
        Room room = new Room();
        room.setType(roomTypeEnum.getType());
        room.setHotFlag(HotFlagEnum.NOT.getType());
        this.save(room);
        return room;
    }
}
