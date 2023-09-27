package com.kkuil.blackchat.mapper;

import com.kkuil.blackchat.domain.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Kkuil
 * @Date 2023/9/27
 * @Description 针对表【role(角色表)】的数据库操作Mapper
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

}
