package com.kkuil.blackchat.core.contact.controller;

import com.kkuil.blackchat.core.contact.domain.vo.response.FriendResp;
import com.kkuil.blackchat.domain.dto.RequestHolderDTO;
import com.kkuil.blackchat.service.UserFriendService;
import com.kkuil.blackchat.utils.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/10/9 21:49
 * @Description 朋友控制器
 */
@RequestMapping("friend")
@RestController
public class FriendController {

    @Resource
    private UserFriendService userFriendService;

    /**
     * 获取朋友列表
     *
     * @return 朋友列表
     */
    @GetMapping("list")
    @Operation(summary = "获取朋友列表", description = "获取朋友列表")
    public ResultUtil<List<FriendResp>> listContact() {
        Long uid = RequestHolderDTO.get().getUid();
        return ResultUtil.success(userFriendService.listFriend(uid));
    }

}
