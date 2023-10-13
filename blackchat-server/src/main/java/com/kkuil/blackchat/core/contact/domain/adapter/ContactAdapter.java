package com.kkuil.blackchat.core.contact.domain.adapter;

import cn.hutool.core.util.ObjectUtil;
import com.kkuil.blackchat.core.contact.domain.vo.response.ChatContactCursorResp;
import com.kkuil.blackchat.domain.bo.contact.ContactWithActiveMsg;
import com.kkuil.blackchat.domain.vo.response.CursorPageBaseResp;

import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/10/5 21:06
 * @Description 会话适配器
 */
public class ContactAdapter {
    /**
     * 构建
     *
     * @param list       列表
     * @param cursorPage 游标对象
     * @return 返回响应
     */
    public static CursorPageBaseResp<ChatContactCursorResp, String> buildContactCursorPage(List<ChatContactCursorResp> list, CursorPageBaseResp<ContactWithActiveMsg, String> cursorPage) {
        CursorPageBaseResp<ChatContactCursorResp, String> chatMemberRespCursorPageBaseResp = new CursorPageBaseResp<>();
        chatMemberRespCursorPageBaseResp.setCursor(cursorPage.getCursor());
        chatMemberRespCursorPageBaseResp.setIsLast(cursorPage.getIsLast());
        chatMemberRespCursorPageBaseResp.setList(list);
        if (ObjectUtil.isNotNull(cursorPage.getExtraInfo())) {
            chatMemberRespCursorPageBaseResp.setExtraInfo(cursorPage.getExtraInfo());
        }
        return chatMemberRespCursorPageBaseResp;
    }
}
