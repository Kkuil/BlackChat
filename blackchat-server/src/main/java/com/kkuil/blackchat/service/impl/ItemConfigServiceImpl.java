package com.kkuil.blackchat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.ItemConfig;
import com.kkuil.blackchat.service.ItemConfigService;
import com.kkuil.blackchat.mapper.ItemConfigMapper;
import org.springframework.stereotype.Service;

/**
* @author 小K
* @description 针对表【item_config(功能物品配置表)】的数据库操作Service实现
* @createDate 2023-09-27 11:55:38
*/
@Service
public class ItemConfigServiceImpl extends ServiceImpl<ItemConfigMapper, ItemConfig>
    implements ItemConfigService{

}
