package com.kkuil.blackchat.mapper;

import com.kkuil.blackchat.domain.entity.Contact;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Kkuil
 * @Date 2023/9/29 13:08
 * @Description 针对表【contact(会话列表)】的数据库操作Mapper
 */
@Mapper
public interface ContactMapper extends BaseMapper<Contact> {

}
