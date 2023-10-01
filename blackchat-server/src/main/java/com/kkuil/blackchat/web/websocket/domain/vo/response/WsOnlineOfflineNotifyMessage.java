package com.kkuil.blackchat.web.websocket.domain.vo.response;

import com.kkuil.blackchat.web.chat.domain.vo.response.ChatMemberResp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/9/27
 * @Description 用户上下线变动的推送类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WsOnlineOfflineNotifyMessage {
    /**
     * 新的上下线用户
     * 只发送新更新的用户信息，其他信息已经在前端本地存储了
     */
    private List<ChatMemberResp> changeList = new ArrayList<>();

    /**
     * 在线人数
     */
    private Long onlineNum;
}
