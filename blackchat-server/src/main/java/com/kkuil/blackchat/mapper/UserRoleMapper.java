package com.kkuil.blackchat.mapper;

import com.kkuil.blackchat.domain.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Kkuil
 * @Date 2023/9/27
 * @Description 针对表【user_role(用户角色关系表)】的数据库操作Mapper
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

}
