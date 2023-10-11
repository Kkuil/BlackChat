package com.kkuil.blackchat.service;

import com.kkuil.blackchat.core.user.domain.vo.request.AddFriendReq;
import com.kkuil.blackchat.core.user.domain.vo.request.CreateGroupReq;
import com.kkuil.blackchat.core.user.domain.vo.request.UserInfoCache;
import com.kkuil.blackchat.core.user.domain.vo.response.UserSearchRespVO;
import com.kkuil.blackchat.domain.common.page.PageReq;
import com.kkuil.blackchat.domain.common.page.PageRes;

import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description 针对表【user(用户表)】的数据库操作Service
 */
public interface UserService {

    /**
     * 用户注册
     *
     * @param openId openid
     */
    void register(String openId);

    /**
     * 更新用户名
     *
     * @param uid      用户ID
     * @param username 用户名
     * @return 是否更改成功
     */
    Boolean updateUsername(Long uid, String username);

    /**
     * 批量获取用户信息
     *
     * @param uidList 用户ID列表
     * @return 用户缓存列表
     */
    List<UserInfoCache> getBatchUserInfoCache(List<Long> uidList);

    /**
     * 搜索用户
     *
     * @param uid     用户ID
     * @param pageReq 分页信息
     * @return 用户列表
     */
    PageRes<List<UserSearchRespVO>> search(Long uid, PageReq<String> pageReq);
}
