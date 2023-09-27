package com.kkuil.blackchat.mapper;

import com.kkuil.blackchat.domain.entity.SensitiveWord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 小K
* @description 针对表【sensitive_word(敏感词库)】的数据库操作Mapper
* @createDate 2023-09-27 11:55:56
* @Entity com.kkuil.blackchat.domain.entity.SensitiveWord
*/
@Mapper
public interface SensitiveWordMapper extends BaseMapper<SensitiveWord> {

}
