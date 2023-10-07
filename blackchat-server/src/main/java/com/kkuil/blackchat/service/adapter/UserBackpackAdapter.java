package com.kkuil.blackchat.service.adapter;

import com.kkuil.blackchat.domain.entity.ItemConfig;
import com.kkuil.blackchat.domain.entity.User;
import com.kkuil.blackchat.domain.enums.YesOrNoEnum;
import com.kkuil.blackchat.domain.vo.response.BadgeBatchReq;

import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/10/1 9:48
 * @Description 用户背包适配器
 */
public class UserBackpackAdapter {

    /**
     * 通过用户ID和用户背包信息进行用户佩戴徽章信息标记
     *
     * @param user               用户信息
     * @param userOwnedBadgesIds 用户徽章信息
     * @param items              徽章信息
     */
    public static List<BadgeBatchReq> buildBatchBadgesByUid(User user, List<Long> userOwnedBadgesIds, List<ItemConfig> items) {
        Long itemIdOwned = user.getItemId();
        return items.stream().map(item -> {
            Long itemId = item.getId();
            int obtains = YesOrNoEnum.NO.getStatus();
            int wearing = YesOrNoEnum.NO.getStatus();
            // 1. 判断是否已经佩戴
            if (itemId.equals(itemIdOwned)) {
                wearing = YesOrNoEnum.YES.getStatus();
            }
            if (userOwnedBadgesIds.contains(itemId)) {
                obtains = YesOrNoEnum.YES.getStatus();
            }
            return BadgeBatchReq
                    .builder()
                    .id(itemId)
                    .img(item.getImg())
                    .obtain(obtains)
                    .wearing(wearing)
                    .describe(item.getDescribe())
                    .build();
        }).toList();
    }

}
