package com.kkuil.blackchat.mapper;

import com.kkuil.blackchat.domain.entity.MessageMark;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 小K
* @description 针对表【message_mark(消息标记表)】的数据库操作Mapper
* @createDate 2023-09-27 11:55:47
* @Entity com.kkuil.blackchat.domain.entity.MessageMark
*/
@Mapper
public interface MessageMarkMapper extends BaseMapper<MessageMark> {

}
