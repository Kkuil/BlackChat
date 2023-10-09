package com.kkuil.blackchat.mapper;

import com.kkuil.blackchat.core.contact.domain.bo.FriendBaseInfoBO;
import com.kkuil.blackchat.domain.entity.UserFriend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/9/29 13:10
 * @Description 针对表【user_friend(用户联系人表)】的数据库操作Mapper
 */
@Mapper
public interface UserFriendMapper extends BaseMapper<UserFriend> {

    /**
     * 批量获取朋友
     *
     * @param uid 用户ID
     * @return 朋友列表
     */
    List<FriendBaseInfoBO> listByUid(Long uid);
}
