package com.kkuil.blackchat.core.contact.domain.fatory.mail.handlers;

import com.kkuil.blackchat.cache.UserCache;
import com.kkuil.blackchat.domain.dto.UserBaseInfo;
import com.kkuil.blackchat.domain.entity.UserApply;
import com.kkuil.blackchat.domain.enums.ReadStatusEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @Author Kkuil
 * @Date 2023/10/13 16:08
 * @Description 未读消息处理器
 */
@Component
public class UnreadContentHandler extends AbstractReadStatusContentHandler {

    @Resource
    private UserCache userCache;

    /**
     * 获取消息处理器状态
     *
     * @return 消息处理器状态
     */
    @Override
    public Integer getStatus() {
        return ReadStatusEnum.UNREAD.getStatus();
    }

    /**
     * 添加朋友申请
     *
     * @param userApply 申请记录
     * @return 内容
     */
    @Override
    public String showContentInAddFriend(UserApply userApply) {
        Long uid = userApply.getUid();
        UserBaseInfo baseInfo = userCache.getBaseUserInfoByUid(uid);
        return String.format("%s申请加你为好友", baseInfo.getName());
    }

    /**
     * 邀请加群申请
     *
     * @param userApply 申请记录
     * @return 内容
     */
    @Override
    public String showContentInInviteGroup(UserApply userApply) {
        Long uid = userApply.getUid();
        UserBaseInfo baseInfo = userCache.getBaseUserInfoByUid(uid);
        return String.format("%s邀请你加入群聊", baseInfo.getName());
    }
}
