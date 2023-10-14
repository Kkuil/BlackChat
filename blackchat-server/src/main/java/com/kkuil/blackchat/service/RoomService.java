package com.kkuil.blackchat.service;

import com.kkuil.blackchat.core.websocket.domain.vo.request.ChatMessageReq;

/**
 * @Author Kkuil
 * @Date 2023/9/28 14:51
 * @Description 针对表【room(房间表)】的数据库操作Service
 */
public interface RoomService {

    /**
     * 检查用户是否存在该房间，该房间是否把他拉黑或者房间是否存在
     *
     * @param uid            用户ID
     * @param chatMessageReq 请求消息体
     */
    void check(Long uid, ChatMessageReq chatMessageReq);

    /**
     * 判断用户是否在存在于同一个房间
     *
     * @param roomId 房间ID
     * @param uids   一群用户ID
     * @return 是否是在群聊中
     */
    Boolean checkRoomMembership(Long roomId, Long... uids);

    /**
     * 创建群聊
     *
     * @param uid       用户ID
     * @param groupName 群名
     */
    void createGroup(Long uid, String groupName);
}
