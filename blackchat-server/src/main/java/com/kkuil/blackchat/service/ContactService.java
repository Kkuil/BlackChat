package com.kkuil.blackchat.service;

import com.kkuil.blackchat.core.contact.domain.vo.request.ChatContactCursorReq;
import com.kkuil.blackchat.core.contact.domain.vo.request.ChatReadMessageReq;
import com.kkuil.blackchat.core.contact.domain.vo.response.ChatContactCursorResp;
import com.kkuil.blackchat.core.contact.domain.vo.response.FriendResp;
import com.kkuil.blackchat.domain.vo.response.CursorPageBaseResp;

import java.util.List;

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

    /**
     * 用户阅读信息上报
     *
     * @param uid     用户ID
     * @param request 上报信息
     * @return 是否已上报
     */
    boolean readMessage(Long uid, ChatReadMessageReq request);
}
