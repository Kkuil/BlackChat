package com.kkuil.blackchat.mapper;

import com.kkuil.blackchat.domain.entity.UserBackpack;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Kkuil
 * @Date 2023/9/29 13:10
 * @Description 针对表【user_backpack(用户背包表)】的数据库操作Mapper
 */
@Mapper
public interface UserBackpackMapper extends BaseMapper<UserBackpack> {

}
