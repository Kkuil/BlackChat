package com.kkuil.blackchat.core.contact.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Kkuil
 * @Date 2023/10/15 15:48
 * @Description 用户申请额外信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserApplyExtraInfo {

    /**
     * 当申请为加群申请时，带上此参数
     */
    private Long groupId;

}
