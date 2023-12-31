package com.kkuil.blackchat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.ItemConfig;
import com.kkuil.blackchat.mapper.ItemConfigMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/9/26 15:55
 * @Description 用户物品访问层
 */
@Service
public class ItemConfigDAO extends ServiceImpl<ItemConfigMapper, ItemConfig> {

    /**
     * 默认分页大小
     */
    public static final Integer DEFAULT_PAGE_SIZE = 10;

    /**
     * 通过物品类型ID获取物品
     *
     * @param type 物品类型ID
     * @return 物品列表
     */
    public List<ItemConfig> getByType(Long current, Integer type) {
        long skip = (current - 1) * DEFAULT_PAGE_SIZE;
        return lambdaQuery().eq(ItemConfig::getType, type).last("limit " + skip + " ," + DEFAULT_PAGE_SIZE).list();
    }

}
