package com.kkuil.blackchat.core.contact.domain.fatory.handlers;

import com.kkuil.blackchat.core.contact.domain.fatory.MailContentReadStatusFactory;
import com.kkuil.blackchat.domain.entity.UserApply;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * @Author Kkuil
 * @Date 2023/10/13 16:08
 * @Description 已读消息处理器
 */
@Component
public abstract class AbstractReadStatusContentHandler {

    @PostConstruct
    public void init() {
        MailContentReadStatusFactory.register(this.getStatus(), this);
    }

    /**
     * 获取消息处理器状态
     *
     * @return 消息处理器状态
     */
    public abstract Integer getStatus();

    /**
     * 添加朋友申请
     *
     * @param userApply 申请记录
     * @return 内容
     */
    public abstract String showContentInAddFriend(UserApply userApply);

    /**
     * 邀请加群申请
     *
     * @param userApply 申请记录
     * @return 内容
     */
    public abstract String showContentInInviteGroup(UserApply userApply);

}
