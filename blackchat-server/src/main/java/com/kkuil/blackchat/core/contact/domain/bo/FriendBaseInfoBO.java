package com.kkuil.blackchat.core.contact.domain.bo;

import com.kkuil.blackchat.domain.dto.IpInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Kkuil
 * @Date 2023/10/9 22:06
 * @Description 批量获取朋友基本信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendBaseInfoBO {

    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 用户归属地信息
     */
    private IpInfo ipInfo;

}
