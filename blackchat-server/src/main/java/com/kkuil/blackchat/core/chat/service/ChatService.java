package com.kkuil.blackchat.core.chat.service;

import com.kkuil.blackchat.domain.vo.response.CursorPageBaseResp;
import com.kkuil.blackchat.utils.ResultUtil;
import com.kkuil.blackchat.core.chat.domain.vo.request.member.ChatMemberCursorReq;
import com.kkuil.blackchat.core.chat.domain.vo.request.message.ChatMessageCursorReq;
import com.kkuil.blackchat.core.chat.domain.vo.response.member.ChatMemberResp;
import com.kkuil.blackchat.core.websocket.domain.vo.request.ChatMessageReq;
import com.kkuil.blackchat.core.chat.domain.vo.response.message.ChatMessageResp;

/**
 * @Author Kkuil
 * @Date 2023/9/28 9:52
 * @Description 聊天接口
 */
public interface ChatService {

    /**
     * 检查是否是临时用户
     *
     * @param uid 用户ID
     * @return 是否是临时用户
     */
    Boolean isTemUser(Long uid);

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
     * @param uid                 用户ID
     * @param chatMemberCursorReq 成员请求信息
     * @return 成员信息
     */
    CursorPageBaseResp<ChatMemberResp> listMember(Long uid, ChatMemberCursorReq chatMemberCursorReq);

    /**
     * 获取消息列表
     *
     * @param chatMessageCursorReq 消息请求参数
     * @param uid                  用户ID
     * @return 响应参数
     */
    CursorPageBaseResp<ChatMessageResp> listMessage(Long uid, ChatMessageCursorReq chatMessageCursorReq);
}
