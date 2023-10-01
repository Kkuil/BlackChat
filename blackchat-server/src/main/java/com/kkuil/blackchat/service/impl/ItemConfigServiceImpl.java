package com.kkuil.blackchat.service.impl;

import com.kkuil.blackchat.cache.ItemCache;
import com.kkuil.blackchat.dao.UserBackpackDAO;
import com.kkuil.blackchat.dao.UserDAO;
import com.kkuil.blackchat.domain.common.page.PageReq;
import com.kkuil.blackchat.domain.entity.ItemConfig;
import com.kkuil.blackchat.domain.entity.User;
import com.kkuil.blackchat.domain.entity.UserBackpack;
import com.kkuil.blackchat.domain.enums.user.ItemTypeEnum;
import com.kkuil.blackchat.domain.vo.response.BadgeBatchReq;
import com.kkuil.blackchat.service.ItemConfigService;
import com.kkuil.blackchat.service.adapter.UserBackpackAdapter;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/10/1 8:48
 * @Description 针对表【item_config(功能物品配置表)】的数据库操作Service实现
 */
@Service
public class ItemConfigServiceImpl implements ItemConfigService {

    @Resource
    private ItemCache itemCache;

    @Resource
    private UserBackpackDAO userBackpackDAO;

    @Resource
    private UserDAO userDao;

    /**
     * 分页查新徽章信息
     *
     * @param pageReq 分页信息
     * @return 徽章信息
     */
    @Override
    public List<BadgeBatchReq> listBadge(Long uid, PageReq<Object> pageReq) {
        // 1. 获取徽章
        List<ItemConfig> badges = itemCache.getByType(pageReq.getCurrent(), ItemTypeEnum.BADGE.getType());
        // 2. 获取用户已拥有的徽章IDs
        List<Long> list = badges.stream().map(ItemConfig::getId).toList();
        List<Long> userOwnedBadgeIds = userBackpackDAO.getByItemIds(uid, list).stream().map(UserBackpack::getItemId).toList();
        // 3. 构建用户已拥有的徽章进行标记
        User user = userDao.getById(uid);
        return UserBackpackAdapter.buildBatchBadgesByUid(user, userOwnedBadgeIds, badges);
    }
}
