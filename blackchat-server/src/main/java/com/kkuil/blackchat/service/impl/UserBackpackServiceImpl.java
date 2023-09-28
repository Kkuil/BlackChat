package com.kkuil.blackchat.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.kkuil.blackchat.cache.ItemCache;
import com.kkuil.blackchat.dao.UserBackpackDAO;
import com.kkuil.blackchat.domain.entity.ItemConfig;
import com.kkuil.blackchat.domain.entity.UserBackpack;
import com.kkuil.blackchat.domain.enums.IdempotentEnum;
import com.kkuil.blackchat.domain.enums.YesOrNoEnum;
import com.kkuil.blackchat.domain.enums.user.ItemTypeEnum;
import com.kkuil.blackchat.service.UserBackpackService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * @Author Kkuil
 * @Date 2023/9/27
 * @Description 针对表【user_backpack(用户背包表)】的数据库操作Service实现
 */
@Service
public class UserBackpackServiceImpl implements UserBackpackService {

    @Resource
    private UserBackpackDAO userBackpackDao;

    @Resource
    private ItemCache itemCache;

    @Resource
    @Lazy
    private UserBackpackServiceImpl userBackpackService;

    /**
     * 用户获取一个物品
     *
     * @param uid            用户id
     * @param itemId         物品id
     * @param idempotentEnum 幂等类型
     * @param businessId     上层业务发送的唯一标识
     */
    @Override
    public void acquireItem(Long uid, Long itemId, IdempotentEnum idempotentEnum, String businessId) {
        String idempotent = getIdempotent(itemId, idempotentEnum, businessId);
        userBackpackService.doAcquireItem(uid, itemId, idempotent);
    }

    public void doAcquireItem(Long uid, Long itemId, String idempotent) {
        // 判断用户是否已经获取过该物品（幂等判断）
        UserBackpack userBackpack = userBackpackDao.getByIdp(idempotent);
        if (ObjectUtil.isNotNull(userBackpack)) {
            // 该用户已经获取过该物品
            return;
        }
        // 判断获取物品的类型是否是徽章类型，因为每人只能获取同一张徽章一次（业务判断）
        ItemConfig itemConfig = itemCache.getById(itemId);
        boolean equals = ItemTypeEnum.BADGE.getType().equals(itemConfig.getType());
        if (equals) {
            // 判断是否
            Integer count = userBackpackDao.getCountByValidItemId(uid, itemId);
            if (count > 0) {
                // 已经有徽章了不发
                return;
            }
        }
        // 发物品
        UserBackpack insert = UserBackpack.builder()
                .uid(uid)
                .itemId(itemId)
                .status(YesOrNoEnum.NO.getStatus())
                .idempotent(idempotent)
                .build();
        userBackpackDao.save(insert);
    }

    /**
     * 拼接幂等号
     *
     * @param itemId         物品ID
     * @param idempotentEnum 物品枚举
     * @param businessId     业务ID
     * @return 幂等号
     */
    private String getIdempotent(Long itemId, IdempotentEnum idempotentEnum, String businessId) {
        return String.format("%d_%d_%s", itemId, idempotentEnum.getType(), businessId);
    }
}
