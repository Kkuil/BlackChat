package com.kkuil.blackchat.domain.bo.user;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author Kkuil
 * @Date 2023/9/4 0:38
 * @Description 存入token中的数据
 */
@Data
@Accessors(chain = true)
@Builder
public class MapDataInToken {
    /**
     * 用户名
     */
    private String username;
}
