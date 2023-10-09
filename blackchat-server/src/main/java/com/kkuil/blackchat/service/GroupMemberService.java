package com.kkuil.blackchat.service;

import com.kkuil.blackchat.domain.entity.GroupMember;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
