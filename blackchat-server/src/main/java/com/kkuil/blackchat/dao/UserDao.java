package com.kkuil.blackchat.dao;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.User;
import com.kkuil.blackchat.domain.vo.response.CursorPageBaseResp;
import com.kkuil.blackchat.mapper.UserMapper;
import com.kkuil.blackchat.utils.CursorUtil;
import com.kkuil.blackchat.web.chat.domain.vo.request.ChatMemberCursorReq;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/9/26 15:55
 * @Description 用户访问层
 */
@Service
public class UserDAO extends ServiceImpl<UserMapper, User> {

    /**
     * 通过openid获取用户
     *
     * @param openId openid
     * @return 用户信息
     */
    public User getByOpenId(String openId) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("open_id", openId);
        return getOne(wrapper);
    }

    /**
     * 游标获取数据
     *
     * @param uidList uidList
     * @param request 游标请求
     * @return 数据
     */
    public CursorPageBaseResp<User> getCursorPage(List<Long> uidList, ChatMemberCursorReq request) {
        return CursorUtil.getCursorPageByMysql(this, request, wrapper -> {
            wrapper.eq(User::getActiveStatus, request.getActiveStatus());
            wrapper.in(CollectionUtil.isNotEmpty(uidList), User::getId, uidList);
        }, User::getLastOptTime);
    }
}
