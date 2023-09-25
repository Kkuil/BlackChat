package com.kkuil.blackchat.domain.dto.user;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author Kkuil
 * @Date 2023/08/11 11:45
 * @Description 修改用户数据传输对象
 */
@Data
@Accessors(chain = true)
public class UpdateUserDTO {
    /**
     * 用户id
     */
    private Integer id;
    /**
     * 用户名
     */
    private String name;
    /**
     * 密码
     */
    private String password;
}
