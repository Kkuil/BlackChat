package com.kkuil.blackchat.mapper;

import com.kkuil.blackchat.domain.entity.Black;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Kkuil
 * @Date 2023/9/29 13:08
 * @Description 针对表【black(黑名单)】的数据库操作Mapper
 */
@Mapper
public interface BlackMapper extends BaseMapper<Black> {

}
