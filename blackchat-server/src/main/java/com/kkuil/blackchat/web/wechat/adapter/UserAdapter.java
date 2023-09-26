package com.kkuil.blackchat.web.wechat.adapter;

import cn.hutool.core.util.RandomUtil;
import com.kkuil.blackchat.domain.entity.User;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;

/**
 * @Author Kkuil
 * @Date 2023/9/26 20:37
 * @Description 用户适配器
 */
public class UserAdapter {

    /**
     * 构建已授权的用户对象
     *
     * @param uid 用户ID
     * @param userInfo 用户信息
     * @return 用户信息
     */
    public static User buildAuthorizeUser(Long uid, WxOAuth2UserInfo userInfo) {
        User user = new User();
        user.setId(uid);
        user.setAvatar(userInfo.getHeadImgUrl());
        user.setName(userInfo.getNickname());
        user.setSex(userInfo.getSex());
        if (userInfo.getNickname().length() > 6) {
            user.setName("名字过长" + RandomUtil.randomInt(100000));
        } else {
            user.setName(userInfo.getNickname());
        }
        return user;
    }
}
