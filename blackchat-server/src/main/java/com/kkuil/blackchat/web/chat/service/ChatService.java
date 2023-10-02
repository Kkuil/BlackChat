package com.kkuil.blackchat.web.chat.service;

import com.kkuil.blackchat.domain.vo.response.CursorPageBaseResp;
import com.kkuil.blackchat.utils.ResultUtil;
import com.kkuil.blackchat.web.chat.domain.vo.request.ChatMemberCursorReq;
import com.kkuil.blackchat.web.chat.domain.vo.response.ChatMemberResp;
import com.kkuil.blackchat.web.websocket.domain.vo.request.ChatMessageReq;
import com.kkuil.blackchat.web.chat.domain.vo.response.ChatMessageResp;

/**
 * @Author Kkuil
 * @Date 2023/9/28 9:52
 * @Description 聊天接口
 */
public interface ChatService {

    /**
     * 发送消息
     *
     * @param chatMessageReq 消息体
     * @param uid            用户ID
     * @return 响应体
     */
    ResultUtil<ChatMessageResp> send(Long uid, ChatMessageReq chatMessageReq);

    /**
     * 获取成员信息
     *
     * @param uid       用户ID
     * @param chatMemberCursorReq 成员请求信息
     * @return 成员信息
     */
    CursorPageBaseResp<ChatMemberResp> listMember(Long uid, ChatMemberCursorReq chatMemberCursorReq);
}
