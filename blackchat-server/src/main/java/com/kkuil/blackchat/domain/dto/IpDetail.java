package com.kkuil.blackchat.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author Kkuil
 * @Date 2023/10/1 17:39
 * @Description 用户ip信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IpDetail implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 注册时的ip
     */
    private String ip;

    /**
     * 最新登录的ip
     */
    private String isp;
    private String isp_id;
    private String city;
    private String city_id;
    private String country;
    private String country_id;
    private String region;
    private String region_id;
}
