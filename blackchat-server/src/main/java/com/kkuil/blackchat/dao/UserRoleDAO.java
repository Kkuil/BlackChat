package com.kkuil.blackchat.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.Role;
import com.kkuil.blackchat.domain.entity.User;
import com.kkuil.blackchat.domain.entity.UserRole;
import com.kkuil.blackchat.mapper.RoleMapper;
import com.kkuil.blackchat.mapper.UserRoleMapper;
import com.kkuil.blackchat.utils.AssertUtil;
import org.springframework.stereotype.Service;

/**
 * @Author Kkuil
 * @Date 2023/9/26 15:55
 * @Description 角色访问层
 */
@Service
public class UserRoleDAO extends ServiceImpl<UserRoleMapper, UserRole> {

    /**
     * 获取当前用户的权限
     *
     * @param user 用户信息
     * @return 权限ID
     */
    public Long getPower(User user) {
        QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", user.getId());
        UserRole userRole = this.getOne(wrapper);
        if (userRole == null) {
            return null;
        }
        Long roleId = userRole.getRoleId();
        AssertUtil.isNotEmpty(roleId, "当前用户存在问题，请稍后再试吧~");
        return userRole.getRoleId();
    }
}
