package com.kkuil.blackchat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.UserApply;
import com.kkuil.blackchat.domain.enums.ReadStatusEnum;
import com.kkuil.blackchat.domain.enums.UserApplyEnum;
import com.kkuil.blackchat.domain.enums.UserApplyStatusEnum;
import com.kkuil.blackchat.mapper.UserApplyMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public Boolean createGroup(Long uid, List<Long> uidList, String msg) {
        List<UserApply> applyList = new ArrayList<>();
        for (Long invitedId : uidList) {
            UserApply userApply = new UserApply();
            userApply.setUid(uid);
            userApply.setType(UserApplyEnum.FRIEND.getType());
            userApply.setTargetId(invitedId);
            userApply.setMsg(msg);
            userApply.setStatus(UserApplyStatusEnum.APPLYING.getStatus());
            userApply.setReadStatus(ReadStatusEnum.UNREAD.getStatus());
            applyList.add(userApply);
        }
        return this.saveBatch(applyList);
    }

    /**
     * 是否有申请记录
     *
     * @param uid       申请人ID
     * @param repliedId 被申请人ID
     * @return
     */
    public Boolean isApplying(Long uid, Long repliedId) {
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
}
