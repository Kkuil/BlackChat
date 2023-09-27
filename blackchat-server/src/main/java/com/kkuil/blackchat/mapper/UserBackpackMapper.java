package com.kkuil.blackchat.mapper;

import com.kkuil.blackchat.domain.entity.UserBackpack;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 小K
* @description 针对表【user_backpack(用户背包表)】的数据库操作Mapper
* @createDate 2023-09-27 11:56:02
* @Entity com.kkuil.blackchat.domain.entity.UserBackpack
*/
@Mapper
public interface UserBackpackMapper extends BaseMapper<UserBackpack> {

}
