package com.kkuil.blackchat.core.websocket.domain.vo.response;

import com.kkuil.blackchat.core.websocket.domain.enums.WsResponseTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author Kkuil
 * @Date 2023/09/17 17:00
 * @Description websocket基础返回json对象结构
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class WsBaseResp<T> {
    /**
     * 返回类型
     *
     * @see WsResponseTypeEnum
     */
    private Integer type;

    /**
     * 返回的数据
     */
    private T data;
}
