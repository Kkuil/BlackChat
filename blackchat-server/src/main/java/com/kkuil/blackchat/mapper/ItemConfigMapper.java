package com.kkuil.blackchat.mapper;

import com.kkuil.blackchat.domain.entity.ItemConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 小K
* @description 针对表【item_config(功能物品配置表)】的数据库操作Mapper
* @createDate 2023-09-27 11:55:38
* @Entity com.kkuil.blackchat.domain.entity.ItemConfig
*/
@Mapper
public interface ItemConfigMapper extends BaseMapper<ItemConfig> {

}
