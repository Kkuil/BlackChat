package com.kkuil.blackchat.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.kkuil.blackchat.anotations.FrequencyControl;
import com.kkuil.blackchat.core.user.domain.vo.request.UserInfoCache;
import com.kkuil.blackchat.core.user.domain.vo.response.UserSearchRespVO;
import com.kkuil.blackchat.domain.common.page.PageReq;
import com.kkuil.blackchat.domain.common.page.PageRes;
import com.kkuil.blackchat.domain.dto.RequestHolderDTO;
import com.kkuil.blackchat.service.UserService;
import com.kkuil.blackchat.utils.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author Kkuil
 * @Date 2023/10/8 18:39
 * @Description 获取用户信息
 */
@RestController
@RequestMapping("/public/user")
public class UserCommonController {
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
    public ResultUtil<List<UserInfoCache>> getBatchUserInfoCache(String uidList) {
        List<Long> list = CollectionUtil.toList(uidList.split(",")).stream().map(Long::parseLong).toList();
        return ResultUtil.success(userService.getBatchUserInfoCache(list));
    }

    /**
     * 搜索用户
     *
     * @param name 用户名
     * @return 用户列表
     */
    @GetMapping("search")
    @FrequencyControl(prefixKey = "search", target = FrequencyControl.Target.IP, time = 10, unit = TimeUnit.SECONDS, count = 5)
    @Operation(summary = "搜索用户", description = "搜索用户")
    public ResultUtil<PageRes<UserSearchRespVO>> search(PageReq<String> pageReq) {
        Long uid = RequestHolderDTO.get().getUid();
        return ResultUtil.success(userService.search(uid, pageReq));
    }

}
