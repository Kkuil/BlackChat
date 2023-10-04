package com.kkuil.blackchat.controller;

import com.kkuil.blackchat.domain.common.page.PageReq;
import com.kkuil.blackchat.domain.dto.RequestHolderDTO;
import com.kkuil.blackchat.domain.vo.response.BadgeBatchReq;
import com.kkuil.blackchat.service.ItemConfigService;
import com.kkuil.blackchat.service.UserService;
import com.kkuil.blackchat.utils.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/10/1 8:44
 * @Description 用户控制器
 */
@RequestMapping("user")
@RestController
@Slf4j
public class UserController {

    @Resource
    private ItemConfigService itemConfigService;

    @Resource
    private UserService userService;

    /**
     * 获取徽章
     *
     * @param pageReq 分页信息
     * @return 徽章
     */
    @GetMapping("badge")
    @Operation(summary = "获取徽章", description = "获取徽章")
    public ResultUtil<List<BadgeBatchReq>> listBadge(@Valid PageReq<Object> pageReq) {
        Long uid = RequestHolderDTO.get().getUid();
        List<BadgeBatchReq> listResultUtil = itemConfigService.listBadge(uid, pageReq);
        return ResultUtil.success(listResultUtil);
    }

    /**
     * 改名
     *
     * @param username 用户名
     * @return 是否改名成功
     */
    @PutMapping("/username")
    @Operation(summary = "用户名修改", description = "用户名修改")
    public ResultUtil<Boolean> updateUsername(String username) {
        Long uid = RequestHolderDTO.get().getUid();
        return ResultUtil.success(userService.updateUsername(uid, username));
    }
}
