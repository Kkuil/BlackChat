package com.kkuil.blackchat.mapper;

import com.kkuil.blackchat.domain.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description 针对表【user(用户表)】的数据库操作Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
