package com.kkuil.blackchat.mapper;

import com.kkuil.blackchat.domain.entity.Black;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 小K
* @description 针对表【black(黑名单)】的数据库操作Mapper
* @createDate 2023-09-27 11:55:28
* @Entity com.kkuil.blackchat.domain.entity.Black
*/
@Mapper
public interface BlackMapper extends BaseMapper<Black> {

}
