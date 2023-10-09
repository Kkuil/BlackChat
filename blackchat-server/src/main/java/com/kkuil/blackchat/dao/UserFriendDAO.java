package com.kkuil.blackchat.dao;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.core.contact.domain.bo.FriendBaseInfoBO;
import com.kkuil.blackchat.core.contact.domain.vo.response.FriendResp;
import com.kkuil.blackchat.domain.dto.IpDetail;
import com.kkuil.blackchat.domain.dto.IpInfo;
import com.kkuil.blackchat.domain.entity.User;
import com.kkuil.blackchat.domain.entity.UserFriend;
import com.kkuil.blackchat.mapper.UserFriendMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Author Kkuil
 * @Date 2023/10/9 21:53
 * @Description 朋友访问层
 */
@Service
public class UserFriendDAO extends ServiceImpl<UserFriendMapper, UserFriend> {

    @Resource
    private UserFriendMapper userFriendMapper;

    /**
     * 批量获取朋友
     *
     * @param uid 用户ID
     * @return 朋友列表
     */
    public List<FriendResp> listByUid(Long uid) {
        List<FriendBaseInfoBO> listFriendBaseInfo = userFriendMapper.listByUid(uid);
        return listFriendBaseInfo.stream().map(info -> {
            FriendResp friendResp = new FriendResp();
            friendResp.setUid(info.getUid());
            IpInfo ipInfo = info.getIpInfo();
            if (ObjectUtil.isNull(ipInfo)) {
                friendResp.setPlace("未知");
            } else {
                if (ObjectUtil.isNotNull(ipInfo.getUpdateIpDetail())) {
                    friendResp.setPlace("未知");
                } else {
                    friendResp.setPlace(ipInfo.getUpdateIpDetail().getCity());
                }
            }
            return friendResp;
        }).toList();
    }
}
