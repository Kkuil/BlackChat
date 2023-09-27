package com.kkuil.blackchat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.MessageMark;
import com.kkuil.blackchat.service.IMessageMarkService;
import com.kkuil.blackchat.mapper.MessageMarkMapper;
import org.springframework.stereotype.Service;

/**
* @author 小K
* @description 针对表【message_mark(消息标记表)】的数据库操作Service实现
* @createDate 2023-09-27 11:55:47
*/
@Service
public class MessageMarkServiceImpl extends ServiceImpl<MessageMarkMapper, MessageMark>
    implements IMessageMarkService {

}
