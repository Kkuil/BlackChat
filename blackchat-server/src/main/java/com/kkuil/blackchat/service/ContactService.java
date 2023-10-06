package com.kkuil.blackchat.service;

import com.kkuil.blackchat.core.contact.domain.vo.request.ChatContactCursorReq;
import com.kkuil.blackchat.core.contact.domain.vo.response.ChatContactCursorResp;
import com.kkuil.blackchat.domain.vo.response.CursorPageBaseResp;

/**
 * @Author Kkuil
 * @Date 2023/10/5 17:14
 * @Description 针对表【contact(会话列表)】的数据库操作Service
 */
public interface ContactService {

    /**
     * 获取会话列表
     *
     * @param uid     用户ID
     * @param request 请求
     * @return 会话列表
     */
    CursorPageBaseResp<ChatContactCursorResp> listContact(Long uid, ChatContactCursorReq request);
}
