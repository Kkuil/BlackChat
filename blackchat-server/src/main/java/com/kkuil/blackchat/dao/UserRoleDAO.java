package com.kkuil.blackchat.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.User;
import com.kkuil.blackchat.domain.entity.UserRole;
import com.kkuil.blackchat.domain.enums.RoleEnum;
import com.kkuil.blackchat.domain.enums.error.CommonErrorEnum;
import com.kkuil.blackchat.mapper.UserRoleMapper;
import com.kkuil.blackchat.utils.AssertUtil;
import org.springframework.stereotype.Service;

import java.util.Set;

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
        LambdaQueryWrapper<UserRole> wrapper = new QueryWrapper<UserRole>().lambda().eq(UserRole::getUid, user.getId());
        UserRole userRole = this.getOne(wrapper);
        if (userRole == null) {
            return 0L;
        }
        Long roleId = userRole.getRoleId();
        AssertUtil.isNotEmpty(roleId, CommonErrorEnum.SYSTEM_ERROR.getMsg());
        return userRole.getRoleId();
    }

    /**
     * 判断用户是否有某些权限
     *
     * @param uid         用户ID
     * @return 是否有
     */
    public Boolean hasAuthorities(Long uid) {
        LambdaQueryChainWrapper<UserRole> wrapper = lambdaQuery().eq(UserRole::getUid, uid);
        UserRole userRole = this.getOne(wrapper);
        Long roleId = userRole.getRoleId();
        Set<Long> roleIds = RoleEnum.CACHE.keySet();
        return roleIds.contains(roleId);
    }
}
