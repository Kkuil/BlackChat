package com.kkuil.blackchat.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.cache.ItemCache;
import com.kkuil.blackchat.domain.entity.ItemConfig;
import com.kkuil.blackchat.domain.entity.UserBackpack;
import com.kkuil.blackchat.domain.enums.YesOrNoEnum;
import com.kkuil.blackchat.domain.enums.user.ItemTypeEnum;
import com.kkuil.blackchat.mapper.UserBackpackMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/9/26 15:55
 * @Description 用户背包访问层
 */
@Service
public class UserBackpackDAO extends ServiceImpl<UserBackpackMapper, UserBackpack> {

    @Resource
    private ItemCache itemCache;

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
     * @param uid    用户ID
     * @param itemId 物品ID
     * @return 条数
     */
    public Integer getCountByUidItemId(Long uid, Long itemId) {
        LambdaQueryWrapper<UserBackpack> wrapper = new QueryWrapper<UserBackpack>().lambda()
                .eq(UserBackpack::getUid, uid)
                .eq(UserBackpack::getItemId, itemId);
        List<UserBackpack> list = this.list(wrapper);
        return list.size();
    }

    /**
     * 通过用户ID和物品类型获取用户物品数
     *
     * @param uid      用户ID
     * @param itemType 物品类型
     * @return 用户物品数
     */
    public List<UserBackpack> listItemByType(Long uid, ItemTypeEnum itemType) {
        LambdaQueryChainWrapper<UserBackpack> wrapper = lambdaQuery().eq(UserBackpack::getUid, uid).select(UserBackpack::getItemId);
        List<UserBackpack> userBackpackList1 = this.list(wrapper);
        List<UserBackpack> userBackpackList2 = userBackpackList1.stream().filter(item -> {
            ItemConfig itemConfig = itemCache.getById(item.getItemId());
            return itemType.getType().equals(itemConfig.getType());
        }).toList();
        return userBackpackList2;
    }

    /**
     * 通过物品ID获取当前用户的未使用的物品
     *
     * @param uid     用户ID
     * @param itemIds 物品ID
     * @return 用户物品获取信息
     */
    public List<UserBackpack> getValidByItemIds(Long uid, List<Long> itemIds) {
        return lambdaQuery()
                .eq(UserBackpack::getUid, uid)
                .in(UserBackpack::getItemId, itemIds)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .list();
    }

    /**
     * 更名
     *
     * @param uid    用户ID
     * @param type   物品ID
     * @param status 更改为的状态
     */
    public void updateStatus(Long uid, Integer itemId, Integer status) {
        UpdateWrapper<UserBackpack> wrapper = new UpdateWrapper<>();
        wrapper.lambda()
                .eq(UserBackpack::getUid, uid)
                .eq(UserBackpack::getItemId, itemId)
                .set(UserBackpack::getStatus, status);
        this.update(wrapper);
    }
}
