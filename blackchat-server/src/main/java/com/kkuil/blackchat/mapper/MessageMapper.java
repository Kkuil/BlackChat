package com.kkuil.blackchat.mapper;

import com.kkuil.blackchat.domain.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 小K
* @description 针对表【message(消息表)】的数据库操作Mapper
* @createDate 2023-09-27 11:55:43
* @Entity com.kkuil.blackchat.domain.entity.Message
*/
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

}
