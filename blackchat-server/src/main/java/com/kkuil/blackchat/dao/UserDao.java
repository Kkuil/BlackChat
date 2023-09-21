package com.kkuil.blackchat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.mapper.UserMapper;
import com.kkuil.blackchat.model.entity.User;
import org.springframework.stereotype.Service;

/**
 * @Author Kkuil
 * @Date 2023/9/17 11:57
 * @Description 用户数据访问层
 */
@Service
public class UserDao extends ServiceImpl<UserMapper, User> {
}
