package com.kkuil.blackchat.service;

import com.kkuil.blackchat.core.user.domain.vo.request.CreateGroupReq;

/**
 * @Author Kkuil
 * @Date 2023/10/9 19:37
 * @Description 针对表【group_member(群成员表)】的数据库操作Service
 */
public interface GroupMemberService {

    /**
     * t退出群组
     *
     * @param uid     用户ID
     * @param groupId 群组ID
     * @return 是否退出
     */
    String exitGroup(Long uid, Long groupId);

    /**
     * 创建群聊
     *
     * @param uid     申请人ID
     * @param uidList 邀请人ID
     * @return 是否创建成功
     */
    String createGroup(Long uid, CreateGroupReq createGroupReq);
}
