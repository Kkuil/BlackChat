package com.kkuil.blackchat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.WxMsg;
import com.kkuil.blackchat.service.IWxMsgService;
import com.kkuil.blackchat.mapper.WxMsgMapper;
import org.springframework.stereotype.Service;

/**
* @author 小K
* @description 针对表【wx_msg(微信消息表)】的数据库操作Service实现
* @createDate 2023-09-27 11:56:11
*/
@Service
public class WxMsgServiceImpl extends ServiceImpl<WxMsgMapper, WxMsg>
    implements IWxMsgService {

}
