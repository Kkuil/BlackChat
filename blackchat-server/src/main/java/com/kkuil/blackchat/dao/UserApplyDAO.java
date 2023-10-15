package com.kkuil.blackchat.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.core.contact.domain.bo.UserApplyExtraInfo;
import com.kkuil.blackchat.core.contact.domain.fatory.mail.MailContentReadStatusFactory;
import com.kkuil.blackchat.core.contact.domain.fatory.mail.handlers.AbstractReadStatusContentHandler;
import com.kkuil.blackchat.core.contact.domain.vo.response.MessageResp;
import com.kkuil.blackchat.domain.common.page.PageReq;
import com.kkuil.blackchat.domain.common.page.PageRes;
import com.kkuil.blackchat.domain.entity.UserApply;
import com.kkuil.blackchat.domain.enums.ReadStatusEnum;
import com.kkuil.blackchat.domain.enums.UserApplyEnum;
import com.kkuil.blackchat.domain.enums.UserApplyStatusEnum;
import com.kkuil.blackchat.mapper.UserApplyMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Author Kkuil
 * @Date 2023/9/26 15:55
 * @Description 用户申请访问层
 */
@Service
public class UserApplyDAO extends ServiceImpl<UserApplyMapper, UserApply> {

    /**
     * 添加好友申请
     *
     * @param uid       申请人ID
     * @param repliedId 被申请人ID
     */
    public Boolean addFriend(Long uid, Long repliedId, String msg) {
        UserApply userApply = new UserApply();
        userApply.setUid(uid);
        userApply.setType(UserApplyEnum.FRIEND.getType());
        userApply.setTargetId(repliedId);
        userApply.setMsg(msg);
        userApply.setStatus(UserApplyStatusEnum.APPLYING.getStatus());
        userApply.setReadStatus(ReadStatusEnum.UNREAD.getStatus());
        return this.save(userApply);
    }

    /**
     * 创建群聊申请
     *
     * @param uid     申请人ID
     * @param uidList 被邀请人
     * @return 是否创建成功
     */
    public Boolean applyCreateGroup(Long uid, List<Long> uidList, String msg) {
        List<UserApply> applyList = new ArrayList<>();
        for (Long invitedId : uidList) {
            UserApply userApply = new UserApply();
            userApply.setUid(uid);
            userApply.setType(UserApplyEnum.GROUP.getType());
            userApply.setTargetId(invitedId);
            userApply.setMsg(msg);
            userApply.setStatus(UserApplyStatusEnum.APPLYING.getStatus());
            userApply.setReadStatus(ReadStatusEnum.UNREAD.getStatus());
            applyList.add(userApply);
        }
        return this.saveBatch(applyList);
    }

    /**
     * 是否有申请好友记录
     *
     * @param uid       申请人ID
     * @param repliedId 被申请人ID
     * @return 是否有申请好友记录
     */
    public Boolean isApplyingFriend(Long uid, Long repliedId) {
        return this.lambdaQuery()
                .eq(UserApply::getUid, uid)
                .eq(UserApply::getTargetId, repliedId)
                .or()
                .eq(UserApply::getTargetId, uid)
                .eq(UserApply::getUid, repliedId)
                .and(wrapper -> {
                    wrapper.eq(UserApply::getType, UserApplyEnum.FRIEND.getType())
                            .eq(UserApply::getStatus, UserApplyStatusEnum.APPLYING.getStatus());
                })
                .exists();
    }

    /**
     * 获取用户收件箱列表
     *
     * @param uid     用户ID
     * @param pageReq 分页信息
     * @return 收件箱列表
     */
    public PageRes<UserApply> listMessage(Long uid, PageReq<String> pageReq) {
        Page<UserApply> userApplyPage = new Page<>(pageReq.getCurrent(), pageReq.getPageSize(), true);
        Page<UserApply> page = this.lambdaQuery()
                .eq(UserApply::getTargetId, uid)
                .eq(UserApply::getReadStatus, Optional.of(Integer.parseInt(pageReq.getData())).orElse(ReadStatusEnum.UNREAD.getStatus()))
                .page(userApplyPage);
        PageRes<UserApply> pageRes = new PageRes<>();
        pageRes.setTotal(page.getTotal())
                .setData(page.getRecords())
                .setCurrent(page.getCurrent())
                .setPageSize(page.getSize());
        return pageRes;
    }

    /**
     * 构建消息体
     *
     * @param userApplyPageRes 消息
     * @return 消息体
     */
    public PageRes<MessageResp> buildMessageResp(PageRes<UserApply> userApplyPageRes) {
        List<MessageResp> list = userApplyPageRes.getData().stream().map(userApply -> {
            MessageResp messageResp = new MessageResp();
            messageResp.setId(userApply.getId());
            messageResp.setUid(userApply.getUid());

            // 获取消息发送者的用户名
            String content;
            AbstractReadStatusContentHandler handler = MailContentReadStatusFactory.getHandler(userApply.getReadStatus());
            if (UserApplyEnum.FRIEND.getType().equals(userApply.getType())) {
                content = handler.showContentInAddFriend(userApply);
            } else {
                content = handler.showContentInInviteGroup(userApply);
            }

            messageResp.setContent(content);
            messageResp.setMsg(userApply.getMsg());
            messageResp.setType(userApply.getType());
            return messageResp;
        }).toList();
        PageRes<MessageResp> messageRespPageRes = new PageRes<>();
        messageRespPageRes.setTotal(userApplyPageRes.getTotal())
                .setData(list)
                .setCurrent(userApplyPageRes.getCurrent())
                .setPageSize(userApplyPageRes.getPageSize());
        return messageRespPageRes;
    }

    /**
     * 消息是否存在
     *
     * @param uid 用户ID
     * @param id  消息ID
     * @return 是否存在
     */
    public UserApply isExist(Long uid, Long id) {
        return this.lambdaQuery()
                .eq(UserApply::getTargetId, uid)
                .eq(UserApply::getId, Optional.of(id).orElse(-99L))
                .one();
    }

    /**
     * 消息是否操作过
     *
     * @param uid 用户ID
     * @param id  消息ID
     * @return 是否操作过
     */
    public Boolean isOperated(Long uid, Long id) {
        return this.lambdaQuery()
                .eq(UserApply::getTargetId, uid)
                .eq(UserApply::getId, Optional.of(id).orElse(-99L))
                .eq(UserApply::getStatus, UserApplyStatusEnum.AGREED.getStatus())
                .or()
                .eq(UserApply::getTargetId, uid)
                .eq(UserApply::getId, Optional.of(id).orElse(-99L))
                .eq(UserApply::getStatus, UserApplyStatusEnum.REFUSE.getStatus())
                .exists();
    }

    /**
     * 操作消息
     *
     * @param id     消息ID
     * @param status 操作类型
     * @return 是否操作成功
     */
    public Boolean operate(Long id, Integer status) {
        UserApply byId = this.getById(id);
        UserApply userApply = new UserApply();
        userApply.setId(id);
        userApply.setUid(byId.getTargetId());
        userApply.setStatus(status);
        userApply.setReadStatus(ReadStatusEnum.READ.getStatus());
        return this.updateById(userApply);
    }

    /**
     * 发出加群邀请
     *
     * @param uid     邀请人ID
     * @param groupId 群ID
     * @param uidList 用户列表ID
     * @return 是否发出邀请成功
     */
    public Boolean inviteGroup(Long uid, Long groupId, List<Long> uidList, String msg) {
        List<UserApply> applyList = new ArrayList<>();
        uidList.forEach(targetId -> {
            UserApply userApply = new UserApply();
            userApply.setUid(uid);
            userApply.setType(UserApplyEnum.GROUP.getType());
            userApply.setTargetId(targetId);
            userApply.setMsg(msg);
            // * 注意：加群申请必须带上这个群ID，不然后续须发找到群
            UserApplyExtraInfo userApplyExtraInfo = new UserApplyExtraInfo();
            userApplyExtraInfo.setGroupId(groupId);
            userApply.setExtraInfo(userApplyExtraInfo);
            userApply.setStatus(UserApplyStatusEnum.APPLYING.getStatus());
            userApply.setReadStatus(ReadStatusEnum.UNREAD.getStatus());
            applyList.add(userApply);
        });
        return this.saveBatch(applyList);
    }
}
