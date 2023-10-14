package com.kkuil.blackchat.core.contact.domain.fatory.mail.handlers;

import com.kkuil.blackchat.domain.entity.UserApply;
import com.kkuil.blackchat.domain.enums.ReadStatusEnum;
import com.kkuil.blackchat.domain.enums.UserApplyStatusEnum;
import org.springframework.stereotype.Component;

/**
 * @Author Kkuil
 * @Date 2023/10/13 16:08
 * @Description 已读消息处理器
 */
@Component
public class ReadContentHandler extends AbstractReadStatusContentHandler {
    /**
     * 获取消息处理器状态
     *
     * @return 消息处理器状态
     */
    @Override
    public Integer getStatus() {
        return ReadStatusEnum.READ.getStatus();
    }

    /**
     * 添加朋友申请
     *
     * @param userApply 申请记录
     * @return 内容
     */
    @Override
    public String showContentInAddFriend(UserApply userApply) {
        if (UserApplyStatusEnum.AGREED.getStatus().equals(userApply.getStatus())) {
            return "已同意申请";
        } else {
            return "已拒绝申请";
        }
    }

    /**
     * 邀请加群申请
     *
     * @param userApply 申请记录
     * @return 内容
     */
    @Override
    public String showContentInInviteGroup(UserApply userApply) {
        if (UserApplyStatusEnum.AGREED.getStatus().equals(userApply.getStatus())) {
            return "已同意加入该群";
        } else {
            return "已拒绝加入该群";
        }
    }
}
