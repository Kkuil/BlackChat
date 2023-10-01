package com.kkuil.blackchat.service;

import com.kkuil.blackchat.domain.common.page.PageReq;
import com.kkuil.blackchat.domain.entity.ItemConfig;
import com.kkuil.blackchat.domain.vo.response.BadgeBatchReq;
import com.kkuil.blackchat.utils.ResultUtil;

import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/10/1 8:49
 * @Description 针对表【item_config(功能物品配置表)】的数据库操作Service
 */
public interface ItemConfigService {

    /**
     * 分页查新徽章信息
     *
     * @param uid     用户ID
     * @param pageReq 分页信息
     * @return 徽章信息
     */
    List<BadgeBatchReq> listBadge(Long uid, PageReq<Object> pageReq);
}
