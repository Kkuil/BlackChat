package com.kkuil.blackchat.core.websocket.service.adapter;

import com.kkuil.blackchat.domain.entity.Message;
import com.kkuil.blackchat.domain.vo.response.CursorPageBaseResp;
import com.kkuil.blackchat.core.chat.domain.vo.response.message.ChatMessageResp;

import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/9/29 10:48
 * @Description 消息适配器
 */
public class MessageAdapter {

    /**
     * 构建返回对象
     *
     * @param messageCursorPageBaseResp 返回消息
     * @param list                      列表
     * @return 对象
     */
    public static CursorPageBaseResp<ChatMessageResp, String> buildChatMessageRespList(CursorPageBaseResp<Message, String> messageCursorPageBaseResp, List<ChatMessageResp> list) {
        CursorPageBaseResp<ChatMessageResp, String> cursorPageBaseResp = new CursorPageBaseResp<>();
        cursorPageBaseResp.setList(list);
        cursorPageBaseResp.setCursor(messageCursorPageBaseResp.getCursor());
        cursorPageBaseResp.setIsLast(messageCursorPageBaseResp.getIsLast());
        return cursorPageBaseResp;
    }
}
