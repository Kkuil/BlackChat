package com.kkuil.blackchat.service;

import com.kkuil.blackchat.core.contact.domain.vo.response.FriendResp;
import com.kkuil.blackchat.domain.entity.UserFriend;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/10/9 21:51
 * @Description 针对表【user_friend(用户联系人表)】的数据库操作Service
 */
public interface UserFriendService {

    /**
     * 获取朋友列表
     *
     * @param uid 用户ID
     * @return 朋友列表
     */
    List<FriendResp> listFriend(Long uid);
}
