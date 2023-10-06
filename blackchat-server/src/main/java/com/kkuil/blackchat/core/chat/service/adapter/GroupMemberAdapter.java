package com.kkuil.blackchat.core.chat.service.adapter;

import com.kkuil.blackchat.domain.entity.User;
import com.kkuil.blackchat.domain.vo.response.CursorPageBaseResp;
import com.kkuil.blackchat.core.chat.domain.vo.response.member.ChatMemberResp;

import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/10/1 19:41
 * @Description 群成员信息适配器
 */
public class GroupMemberAdapter {

    /**
     * 构造群成员体
     *
     * @param list 群成员信息
     * @param groupMemberCursorPageBaseResp 群成员列表
     * @return 最终信息
     */
    public static CursorPageBaseResp<ChatMemberResp> buildChatMemberCursorPage(List<ChatMemberResp> list, CursorPageBaseResp<User> groupMemberCursorPageBaseResp) {
        CursorPageBaseResp<ChatMemberResp> chatMemberRespCursorPageBaseResp = new CursorPageBaseResp<>();
        chatMemberRespCursorPageBaseResp.setCursor(groupMemberCursorPageBaseResp.getCursor());
        chatMemberRespCursorPageBaseResp.setIsLast(groupMemberCursorPageBaseResp.getIsLast());
        chatMemberRespCursorPageBaseResp.setList(list);
        chatMemberRespCursorPageBaseResp.setExtraInfo(groupMemberCursorPageBaseResp.getExtraInfo());
        return chatMemberRespCursorPageBaseResp;
    }
}
