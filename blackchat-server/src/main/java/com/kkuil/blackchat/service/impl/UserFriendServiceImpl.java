package com.kkuil.blackchat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.kkuil.blackchat.core.contact.domain.vo.response.FriendResp;
import com.kkuil.blackchat.dao.UserFriendDAO;
import com.kkuil.blackchat.domain.entity.User;
import com.kkuil.blackchat.service.UserFriendService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/10/9 21:51
 * @Description 针对表【user_friend(用户联系人表)】的数据库操作Service实现
 */
@Service
public class UserFriendServiceImpl implements UserFriendService {

    @Resource
    private UserFriendDAO userFriendDao;

    /**
     * 获取朋友列表
     *
     * @param uid 用户ID
     * @return 朋友列表
     */
    @Override
    public List<FriendResp> listFriend(Long uid) {
        return userFriendDao.listByUid(uid);
    }
}
