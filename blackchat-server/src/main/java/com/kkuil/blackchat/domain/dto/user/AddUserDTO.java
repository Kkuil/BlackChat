package com.kkuil.blackchat.domain.dto.user;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author Kkuil
 * @Date 2023/08/11 11:45
 * @Description 添加用戶数据传输对象
 */

@Data
@Accessors(chain = true)
public class AddUserDTO {
    /**
     * 用户名
     */
    private String name;
    /**
     * 密码
     */
    private String password;
}
