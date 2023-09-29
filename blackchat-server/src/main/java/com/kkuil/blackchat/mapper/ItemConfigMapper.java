package com.kkuil.blackchat.mapper;

import com.kkuil.blackchat.domain.entity.ItemConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Kkuil
 * @Date 2023/9/29 13:09
 * @Description 针对表【item_config(功能物品配置表)】的数据库操作Mapper
 */
@Mapper
public interface ItemConfigMapper extends BaseMapper<ItemConfig> {

}
