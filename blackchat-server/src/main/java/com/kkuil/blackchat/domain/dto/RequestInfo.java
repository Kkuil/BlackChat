package com.kkuil.blackchat.domain.dto;

import lombok.Data;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description web请求信息收集类
 */
@Data
public class RequestInfo {
    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 用户IP
     */
    private String ip;
}
