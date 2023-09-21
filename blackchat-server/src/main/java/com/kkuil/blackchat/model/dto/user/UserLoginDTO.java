package com.kkuil.blackchat.model.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Author Kkuil
 * @Date 2023/9/3 16:37
 * @Description 用户登录提交信息
 */
@Data
public class UserLoginDTO {
    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    private String password;
}
