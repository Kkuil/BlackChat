package com.kkuil.blackchat.mapper;

import com.kkuil.blackchat.domain.entity.WxMsg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 小K
* @description 针对表【wx_msg(微信消息表)】的数据库操作Mapper
* @createDate 2023-09-27 11:56:11
* @Entity com.kkuil.blackchat.domain.entity.WxMsg
*/
@Mapper
public interface WxMsgMapper extends BaseMapper<WxMsg> {

}
