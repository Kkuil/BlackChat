package com.kkuil.blackchat.core.user.controller;

import com.kkuil.blackchat.core.user.domain.vo.request.UserInfoCache;
import com.kkuil.blackchat.service.UserService;
import com.kkuil.blackchat.utils.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/10/8 18:39
 * @Description 获取用户信息
 */
@RestController
@RequestMapping("/public/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 批量获取用户缓存
     *
     * @param uidList 用户ID列表
     * @return 用户列表缓存
     */
    @GetMapping("batch-cache")
    @Operation(summary = "批量获取缓存", description = "批量获取缓存")
    public ResultUtil<List<UserInfoCache>> getBatchUserInfoCache(List<Long> uidList) {
        return ResultUtil.success(userService.getBatchUserInfoCache(uidList));
    }

}