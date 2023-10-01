package com.kkuil.blackchat.web.websocket.domain.vo.response;

import lombok.Data;

/**
 * @Author Kkuil
 * @Date 2023/9/30 23:29
 * @Description 更新上线列表
 */
@Data
public class WsUpdateOnlineListResp {
    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 状态 0 下线 1 上线
     */
    private Integer status;
}
