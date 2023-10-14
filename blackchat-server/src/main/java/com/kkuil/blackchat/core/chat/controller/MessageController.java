package com.kkuil.blackchat.core.chat.controller;

import com.kkuil.blackchat.anotations.FrequencyControl;
import com.kkuil.blackchat.core.chat.domain.vo.request.member.ChatMemberCursorReq;
import com.kkuil.blackchat.core.chat.domain.vo.request.message.ChatMessageCursorReq;
import com.kkuil.blackchat.core.chat.domain.vo.request.message.RevokeMessageReq;
import com.kkuil.blackchat.core.chat.domain.vo.response.member.ChatMemberResp;
import com.kkuil.blackchat.core.chat.domain.vo.response.message.ChatMessageResp;
import com.kkuil.blackchat.core.chat.service.ChatService;
import com.kkuil.blackchat.core.websocket.domain.vo.request.ChatMessageReq;
import com.kkuil.blackchat.domain.dto.RequestHolderDTO;
import com.kkuil.blackchat.domain.vo.response.CursorPageBaseResp;
import com.kkuil.blackchat.service.MessageService;
import com.kkuil.blackchat.utils.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author Kkuil
 * @Date 2023/9/27 11:49
 * @Description 聊天控制器
 */
@RestController
@Slf4j
@RequestMapping("message")
public class MessageController {

    @Resource
    private ChatService chatService;

    @Resource
    private MessageService messageService;

    /**
     * 获取消息列表接口
     *
     * @param request 消息请求
     * @return 消息列表
     */
    @GetMapping("list")
    @Operation(summary = "消息列表", description = "消息列表")
    public ResultUtil<CursorPageBaseResp<ChatMessageResp, String>> listMessage(@Valid ChatMessageCursorReq request) {
        Long uid = RequestHolderDTO.get().getUid();
        CursorPageBaseResp<ChatMessageResp, String> messageList = chatService.listMessage(uid, request);
        return ResultUtil.success(messageList);
    }

    /**
     * 撤回消息
     *
     * @param request 消息请求
     * @return 消息列表
     */
    @PutMapping("revoke")
    @Operation(summary = "撤回消息", description = "撤回消息")
    public ResultUtil<Boolean> revoke(@RequestBody @Valid RevokeMessageReq request) {
        Long uid = RequestHolderDTO.get().getUid();
        return ResultUtil.success(messageService.revoke(uid, request));
    }
}
