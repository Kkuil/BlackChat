package com.kkuil.blackchat.mapper;

import com.kkuil.blackchat.domain.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Kkuil
 * @Date 2023/9/29 13:09
 * @Description 针对表【message(消息表)】的数据库操作Mapper
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

}
