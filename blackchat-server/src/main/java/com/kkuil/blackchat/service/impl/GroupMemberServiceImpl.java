package com.kkuil.blackchat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.GroupMember;
import com.kkuil.blackchat.service.GroupMemberService;
import com.kkuil.blackchat.mapper.GroupMemberMapper;
import org.springframework.stereotype.Service;

/**
* @author 小K
* @description 针对表【group_member(群成员表)】的数据库操作Service实现
* @createDate 2023-09-28 14:45:52
*/
@Service
public class GroupMemberServiceImpl extends ServiceImpl<GroupMemberMapper, GroupMember>
    implements GroupMemberService{

}
