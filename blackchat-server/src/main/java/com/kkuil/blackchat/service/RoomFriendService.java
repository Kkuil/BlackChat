package com.kkuil.blackchat.service;

import com.kkuil.blackchat.core.contact.domain.vo.response.FriendResp;
import com.kkuil.blackchat.core.user.domain.vo.request.AddFriendReq;

import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/10/10 14:08
 * @Description 针对表【room_friend(单聊房间表)】的数据库操作Service
 */
public interface RoomFriendService {

    /**
     * 获取朋友列表
     *
     * @param uid 用户ID
     * @return 朋友列表
     */
    List<FriendResp> listFriend(Long uid);

    /**
     * 删除用户
     *
     * @param uid      用户ID
     * @param friendId 朋友ID
     * @return 是否删除成功
     */
    String delFriend(Long uid, Long friendId);


    /**
     * 加好友
     *
     * @param uid       用户ID
     * @param repliedId 被申请人ID
     * @param msg       申请备注
     * @return 是否添加成功
     */
    String addFriend(Long uid, AddFriendReq addFriendReq);
}
