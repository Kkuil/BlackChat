package com.kkuil.blackchat.cache;

import com.kkuil.blackchat.dao.ItemConfigDAO;
import com.kkuil.blackchat.domain.entity.ItemConfig;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/9/27
 * @Description 用户物品相关缓存
 */
@Component
public class ItemCache {

    @Resource
    private ItemConfigDAO itemConfigDao;

    /**
     * 将查询出来的类型物品进行缓存
     *
     * @param type 物品类型 1 2
     * @return 物品列表
     */
    @Cacheable(cacheNames = "item", key = "'itemsByType:'+#type")
    public List<ItemConfig> getByType(Integer type) {
        return itemConfigDao.getByType(type);
    }

    /**
     * 缓存每个物品
     *
     * @param itemId 物品ID
     * @return 物品信息
     */
    @Cacheable(cacheNames = "item", key = "'item:'+#itemId")
    public ItemConfig getById(Long itemId) {
        return itemConfigDao.getById(itemId);
    }
}
