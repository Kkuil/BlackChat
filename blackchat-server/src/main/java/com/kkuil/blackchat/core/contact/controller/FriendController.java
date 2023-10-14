package com.kkuil.blackchat.core.contact.controller;

import com.kkuil.blackchat.core.contact.domain.vo.response.FriendResp;
import com.kkuil.blackchat.core.user.domain.vo.request.AddFriendReq;
import com.kkuil.blackchat.domain.dto.RequestHolderDTO;
import com.kkuil.blackchat.service.RoomFriendService;
import com.kkuil.blackchat.utils.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

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
    private RoomFriendService roomFriendService;

    /**
     * 获取朋友列表
     *
     * @return 朋友列表
     */
    @GetMapping("list")
    @Operation(summary = "获取朋友列表", description = "获取朋友列表")
    public ResultUtil<List<FriendResp>> listContact() {
        Long uid = RequestHolderDTO.get().getUid();
        return ResultUtil.success(roomFriendService.listFriend(uid));
    }

    /**
     * 删除好友
     *
     * @param friendId 朋友ID
     */
    @DeleteMapping("del")
    @Operation(summary = "删除好友", description = "删除好友")
    public ResultUtil<Boolean> delFriend(Long friendId) {
        Long uid = RequestHolderDTO.get().getUid();
        return ResultUtil.success(roomFriendService.delFriend(uid, friendId), true);
    }

    /**
     * 加好友
     *
     * @param addFriendReq AddFriendReq
     * @return 是否改名成功
     */
    @PostMapping("add-friend")
    @Operation(summary = "加好友", description = "加好友")
    public ResultUtil<Boolean> addFriend(@RequestBody AddFriendReq addFriendReq) {
        Long uid = RequestHolderDTO.get().getUid();
        return ResultUtil.success(roomFriendService.addFriend(uid, addFriendReq), true);
    }

}
