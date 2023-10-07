package com.kkuil.blackchat.domain.bo.room;

import lombok.Data;

/**
 * @Author Kkuil
 * @Date 2023/10/5 19:58
 * @Description 房间基本信息
 */
@Data
public class RoomBaseInfo {
    /**
     * 房间类型 1群聊 2单聊
     */
    private Integer type;

    /**
     * 是否全员展示 0否 1是
     */
    private Integer hotFlag;

    /**
     * 额外信息（根据不同类型房间有不同存储的东西）
     */
    private Object extJson;
}
