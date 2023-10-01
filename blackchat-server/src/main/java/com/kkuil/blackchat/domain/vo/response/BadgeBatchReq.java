package com.kkuil.blackchat.domain.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Kkuil
 * @Date 2023/10/1 9:05
 * @Description 徽章返回实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BadgeBatchReq {

    /**
     * 徽章id
     */
    private Long id;

    /**
     * 徽章图标
     */
    private String img;

    /**
     * 徽章描述
     */
    private String describe;

    /**
     * 是否拥有 0否 1是
     */
    private Integer obtain;

    /**
     * 是否佩戴  0否 1是
     */
    private Integer wearing;
}
