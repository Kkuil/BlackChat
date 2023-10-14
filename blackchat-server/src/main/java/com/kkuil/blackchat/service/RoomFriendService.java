package com.kkuil.blackchat.service;

import com.kkuil.blackchat.core.contact.domain.vo.response.FriendResp;
import com.kkuil.blackchat.core.user.domain.vo.request.AddFriendReq;
import com.kkuil.blackchat.domain.entity.UserApply;

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
     * @param uid          用户ID
     * @param addFriendReq AddFriendReq
     * @return 是否添加成功
     */
    String applyAddFriend(Long uid, AddFriendReq addFriendReq);

    /**
     * 同意加好友
     *
     * @param userApply 好友申请记录
     */
    void agreeAddFriend(UserApply userApply);
}
