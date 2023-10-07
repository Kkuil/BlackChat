package com.kkuil.blackchat.mapper;

import com.kkuil.blackchat.domain.entity.GroupMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Kkuil
 * @Date 2023/9/29 13:08
 * @Description 针对表【group_member(群成员表)】的数据库操作Mapper
 */
@Mapper
public interface GroupMemberMapper extends BaseMapper<GroupMember> {

}
