package com.kkuil.blackchat.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.User;
import com.kkuil.blackchat.domain.entity.UserBackpack;
import com.kkuil.blackchat.domain.entity.UserRole;
import com.kkuil.blackchat.mapper.UserBackpackMapper;
import com.kkuil.blackchat.mapper.UserRoleMapper;
import com.kkuil.blackchat.utils.AssertUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/9/26 15:55
 * @Description 用户背包访问层
 */
@Service
public class UserBackpackDAO extends ServiceImpl<UserBackpackMapper, UserBackpack> {

    /**
     * 通过幂等号进行查询数据
     *
     * @param idempotent 幂等号
     * @return 背包数据
     */
    public UserBackpack getByIdp(String idempotent) {
        LambdaQueryWrapper<UserBackpack> wrapper = new QueryWrapper<UserBackpack>().lambda().eq(UserBackpack::getIdempotent, idempotent);
        return this.getOne(wrapper);
    }

    /**
     * 通过用户ID和物品ID获取数据条数
     *
     * @param uid 用户ID
     * @param itemId 物品ID
     * @return 次数
     */
    public Integer getCountByValidItemId(Long uid, Long itemId) {
        LambdaQueryWrapper<UserBackpack> wrapper = new QueryWrapper<UserBackpack>().lambda()
                .eq(UserBackpack::getUid, uid)
                .eq(UserBackpack::getItemId, itemId);
        List<UserBackpack> list = this.list(wrapper);
        return list.size();
    }
}
