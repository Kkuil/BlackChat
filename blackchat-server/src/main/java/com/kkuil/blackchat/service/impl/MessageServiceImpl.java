package com.kkuil.blackchat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.Message;
import com.kkuil.blackchat.service.IMessageService;
import com.kkuil.blackchat.mapper.MessageMapper;
import org.springframework.stereotype.Service;

/**
* @author 小K
* @description 针对表【message(消息表)】的数据库操作Service实现
* @createDate 2023-09-27 11:55:43
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements IMessageService {

}
