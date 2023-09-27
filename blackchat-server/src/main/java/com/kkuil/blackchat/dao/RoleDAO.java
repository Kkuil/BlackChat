package com.kkuil.blackchat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.Role;
import com.kkuil.blackchat.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/**
 * @Author Kkuil
 * @Date 2023/9/26 15:55
 * @Description 角色访问层
 */
@Service
public class RoleDAO extends ServiceImpl<RoleMapper, Role> {

}
