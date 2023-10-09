package com.kkuil.blackchat.core.contact.controller;

import com.kkuil.blackchat.core.contact.domain.vo.request.ChatContactCursorReq;
import com.kkuil.blackchat.core.contact.domain.vo.request.ChatReadMessageReq;
import com.kkuil.blackchat.core.contact.domain.vo.response.ChatContactCursorResp;
import com.kkuil.blackchat.core.contact.domain.vo.response.FriendResp;
import com.kkuil.blackchat.domain.dto.RequestHolderDTO;
import com.kkuil.blackchat.domain.vo.response.CursorPageBaseResp;
import com.kkuil.blackchat.service.ContactService;
import com.kkuil.blackchat.service.UserFriendService;
import com.kkuil.blackchat.utils.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/10/5 17:09
 * @Description 会话控制器
 */
@RestController
@RequestMapping("contact")
public class ContactController {
    @Resource
    private ContactService contactService;

    @Resource
    private UserFriendService userFriendService;

    /**
     * 获取会话列表
     *
     * @param request 请求信息
     * @return 会话列表
     */
    @GetMapping("list")
    @Operation(summary = "获取会话列表", description = "获取会话列表")
    public ResultUtil<CursorPageBaseResp<ChatContactCursorResp>> listContact(@Valid ChatContactCursorReq request) {
        Long uid = RequestHolderDTO.get().getUid();
        return ResultUtil.success(contactService.listContact(uid, request));
    }

    /**
     * 用户已读上报
     *
     * @param request 用户上报信息
     * @return 上报
     */
    @PostMapping("read-message")
    @Operation(summary = "用户已读上报", description = "用户已读上报")
    public ResultUtil<Boolean> readMessage(@Valid @RequestBody ChatReadMessageReq request) {
        Long uid = RequestHolderDTO.get().getUid();
        return ResultUtil.success(contactService.readMessage(uid, request));
    }
}
