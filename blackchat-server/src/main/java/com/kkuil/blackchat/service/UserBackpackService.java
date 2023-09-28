package com.kkuil.blackchat.service;

import com.kkuil.blackchat.domain.enums.IdempotentEnum;

/**
 * @Author Kkuil
 * @Date 2023/9/27
 * @Description 针对表【user_backpack(用户背包表)】的数据库操作Service
 */
public interface UserBackpackService {

    /**
     * 用户获取一个物品
     *
     * @param uid            用户id
     * @param itemId         物品id
     * @param idempotentEnum 幂等类型
     * @param businessId     上层业务发送的唯一标识
     */
    void acquireItem(Long uid, Long itemId, IdempotentEnum idempotentEnum, String businessId);
}
