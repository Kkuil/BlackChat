package com.kkuil.blackchat.service;

import com.kkuil.blackchat.core.contact.domain.vo.request.AddAdminReq;
import com.kkuil.blackchat.core.contact.domain.vo.request.DelAdminReq;
import com.kkuil.blackchat.core.contact.domain.vo.request.InvitAddGroupReq;
import com.kkuil.blackchat.core.user.domain.vo.request.CreateGroupReq;
import com.kkuil.blackchat.domain.entity.UserApply;

/**
 * @Author Kkuil
 * @Date 2023/10/9 19:37
 * @Description 针对表【group_member(群成员表)】的数据库操作Service
 */
public interface GroupMemberService {

    /**
     * 退出群组
     *
     * @param uid     用户ID
     * @param groupId 群组ID
     * @return 是否退出
     */
    String exitGroup(Long uid, Long groupId);

    /**
     * 创建群聊
     *
     * @param uid            申请人ID
     * @param createGroupReq CreateGroupReq
     * @return 是否创建成功
     */
    String createGroup(Long uid, CreateGroupReq createGroupReq);

    /**
     * 同意加群
     *
     * @param userApply 用户申请记录
     */
    void agreeAddGroup(UserApply userApply);

    /**
     * 邀请加群
     *
     * @param uid              邀请人ID
     * @param invitAddGroupReq 邀请加群的请求信息
     * @return 是否邀请成功
     */
    Boolean inviteGroup(Long uid, InvitAddGroupReq invitAddGroupReq);

    /**
     * 添加管理
     *
     * @param uid         用户ID
     * @param addAdminReq 请求信息
     * @return 是否添加成功
     */
    Boolean addAdmin(Long uid, AddAdminReq addAdminReq);

    /**
     * 删除管理
     *
     * @param uid         用户ID
     * @param delAdminReq 请求信息
     * @return 是否删除成功
     */
    Boolean delAdmin(Long uid, DelAdminReq delAdminReq);
}
