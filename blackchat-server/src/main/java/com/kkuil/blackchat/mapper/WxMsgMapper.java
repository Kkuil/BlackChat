package com.kkuil.blackchat.mapper;

import com.kkuil.blackchat.domain.entity.WxMsg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Kkuil
 * @Date 2023/9/29 13:11
 * @Description 针对表【wx_msg(微信消息表)】的数据库操作Mapper
 */
@Mapper
public interface WxMsgMapper extends BaseMapper<WxMsg> {

}
