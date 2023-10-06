package com.kkuil.blackchat.core.mq.domain.dto;

import com.kkuil.blackchat.core.mq.domain.enums.WsPushTypeEnum;
import com.kkuil.blackchat.core.websocket.domain.vo.response.WsBaseResp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author Kkuil
 * @Date 2023/10/6 9:28
 * @Description 推送给用户的消息对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PushMessageDTO implements Serializable {
    /**
     * 推送的ws消息
     */
    private WsBaseResp<?> wsBaseMsg;

    /**
     * 推送的uid
     */
    private Long uid;

    /**
     * 推送类型 1个人 2全员
     *
     * @see WsPushTypeEnum
     */
    private Integer pushType;

    public PushMessageDTO(Long uid, WsBaseResp<?> wsBaseMsg) {
        this.uid = uid;
        this.wsBaseMsg = wsBaseMsg;
        this.pushType = WsPushTypeEnum.USER.getType();
    }

    public PushMessageDTO(WsBaseResp<?> wsBaseMsg) {
        this.wsBaseMsg = wsBaseMsg;
        this.pushType = WsPushTypeEnum.ALL.getType();
    }
}
