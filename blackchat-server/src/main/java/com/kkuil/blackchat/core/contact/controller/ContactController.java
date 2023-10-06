package com.kkuil.blackchat.core.contact.controller;

import com.kkuil.blackchat.core.contact.domain.vo.request.ChatContactCursorReq;
import com.kkuil.blackchat.core.contact.domain.vo.response.ChatContactCursorResp;
import com.kkuil.blackchat.domain.dto.RequestHolderDTO;
import com.kkuil.blackchat.domain.vo.response.CursorPageBaseResp;
import com.kkuil.blackchat.service.ContactService;
import com.kkuil.blackchat.utils.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

    @GetMapping("list")
    @Operation(summary = "获取联系人列表", description = "获取联系人列表")
    public ResultUtil<CursorPageBaseResp<ChatContactCursorResp>> listContact(@Valid ChatContactCursorReq request) {
        Long uid = RequestHolderDTO.get().getUid();
        return ResultUtil.success(contactService.listContact(uid, request));
    }
}
