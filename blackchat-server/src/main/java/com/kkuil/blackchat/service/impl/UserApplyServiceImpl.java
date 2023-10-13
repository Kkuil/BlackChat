package com.kkuil.blackchat.service.impl;

import com.kkuil.blackchat.core.contact.domain.vo.request.MessageOperationReq;
import com.kkuil.blackchat.core.contact.domain.vo.response.MessageResp;
import com.kkuil.blackchat.dao.UserApplyDAO;
import com.kkuil.blackchat.domain.common.page.PageReq;
import com.kkuil.blackchat.domain.common.page.PageRes;
import com.kkuil.blackchat.domain.entity.UserApply;
import com.kkuil.blackchat.domain.enums.error.ChatErrorEnum;
import com.kkuil.blackchat.domain.enums.error.CommonErrorEnum;
import com.kkuil.blackchat.service.UserApplyService;
import com.kkuil.blackchat.utils.AssertUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author Kkuil
 * @Date 2023/10/12 23:02
 * @Description 针对表【user_apply(用户申请表)】的数据库操作Service实现
 */
@Service
public class UserApplyServiceImpl implements UserApplyService {

    @Resource
    private UserApplyDAO userApplyDao;

    /**
     * 获取用户收件箱列表
     *
     * @param uid     用户ID
     * @param pageReq 分页信息
     * @return 收件箱列表
     */
    @Override
    public PageRes<MessageResp> listMessage(Long uid, PageReq<String> pageReq) {
        PageRes<UserApply> userApplyPageRes = userApplyDao.listMessage(uid, pageReq);
        return userApplyDao.buildMessageResp(userApplyPageRes);
    }

    /**
     * 消息操作
     *
     * @param uid                 用户ID
     * @param messageOperationReq 消息操作请求体
     * @return 是否操作成功
     */
    @Override
    public Boolean operation(Long uid, MessageOperationReq messageOperationReq) {
        Long id = messageOperationReq.getId();

        // 1. 判断消息是否存在且是否是该用户的
        Boolean isExist = userApplyDao.isExist(uid, id);
        AssertUtil.isTrue(isExist, ChatErrorEnum.MESSAGE_NOT_EXIST.getMsg());

        // 2. 判断消息是否已被操作
        Boolean isOperated = userApplyDao.isOperated(uid, id);
        AssertUtil.isFalse(isOperated, ChatErrorEnum.REPEAT_OPERATED.getMsg());

        // 3. 操作消息
        Boolean isOperatedSuccess = userApplyDao.operate(uid, id, messageOperationReq.getStatus());
        AssertUtil.isTrue(isOperatedSuccess, CommonErrorEnum.SYSTEM_ERROR.getMsg());

        return true;
    }
}
