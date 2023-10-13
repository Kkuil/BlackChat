package com.kkuil.blackchat.service;

import com.kkuil.blackchat.core.contact.domain.vo.request.MessageOperationReq;
import com.kkuil.blackchat.core.contact.domain.vo.request.MessageReq;
import com.kkuil.blackchat.core.contact.domain.vo.response.MessageResp;
import com.kkuil.blackchat.domain.common.page.PageReq;
import com.kkuil.blackchat.domain.common.page.PageRes;
import com.kkuil.blackchat.domain.entity.UserApply;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/10/12 23:01
 * @Description 针对表【user_apply(用户申请表)】的数据库操作Service
 */
public interface UserApplyService {

    /**
     * 获取用户收件箱列表
     *
     * @param uid     用户ID
     * @param pageReq 分页信息
     * @return 收件箱列表
     */
    PageRes<MessageResp> listMessage(Long uid, PageReq<String> pageReq);

    /**
     * 消息操作
     *
     * @param uid                 用户ID
     * @param messageOperationReq 消息操作请求体
     * @return 是否操作成功
     */
    Boolean operation(Long uid, MessageOperationReq messageOperationReq);
}
