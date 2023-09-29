package com.kkuil.blackchat.mapper;

import com.kkuil.blackchat.domain.entity.SensitiveWord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Kkuil
 * @Date 2023/9/29 13:10
 * @Description 针对表【sensitive_word(敏感词库)】的数据库操作Mapper
 */
@Mapper
public interface SensitiveWordMapper extends BaseMapper<SensitiveWord> {

}
