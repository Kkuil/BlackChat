package com.kkuil.blackchat.core.contact.domain.bo;

import lombok.Data;

/**
 * @Author Kkuil
 * @Date 2023/10/15 15:48
 * @Description 用户申请额外信息
 */
@Data
public class UserApplyExtraInfo {

    /**
     * 当申请为加群申请时，带上此参数
     */
    private Long groupId;

}
