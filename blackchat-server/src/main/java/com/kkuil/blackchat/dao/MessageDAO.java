package com.kkuil.blackchat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.Message;
import com.kkuil.blackchat.mapper.MessageMapper;
import org.springframework.stereotype.Service;

/**
 * @Author Kkuil
 * @Date 2023/9/28 18:28
 * @Description 消息访问层
 */
@Service
public class MessageDAO extends ServiceImpl<MessageMapper, Message> {
}
