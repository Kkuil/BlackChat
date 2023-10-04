package com.kkuil.blackchat.web.chat.controller;

import com.kkuil.blackchat.anotations.FrequencyControl;
import com.kkuil.blackchat.domain.dto.RequestHolderDTO;
import com.kkuil.blackchat.domain.vo.response.CursorPageBaseResp;
import com.kkuil.blackchat.utils.ResultUtil;
import com.kkuil.blackchat.web.chat.domain.vo.request.member.ChatMemberCursorReq;
import com.kkuil.blackchat.web.chat.domain.vo.request.message.ChatMessageCursorReq;
import com.kkuil.blackchat.web.chat.domain.vo.response.member.ChatMemberResp;
import com.kkuil.blackchat.web.websocket.domain.vo.request.ChatMessageReq;
import com.kkuil.blackchat.web.chat.domain.vo.response.message.ChatMessageResp;
import com.kkuil.blackchat.web.chat.service.ChatService;
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
@RequestMapping("chat")
public class ChatController {

    @Resource
    private ChatService chatService;

    /**
     * 发送消息接口
     *
     * @param chatMessageReq 消息体
     * @return 消息返回体
     */
    @PostMapping("send")
    @FrequencyControl(time = 5, count = 3, target = FrequencyControl.Target.UID)
    @FrequencyControl(time = 30, count = 5, target = FrequencyControl.Target.UID)
    @FrequencyControl(time = 60, count = 10, target = FrequencyControl.Target.UID)
    public ResultUtil<ChatMessageResp> send(@Valid @RequestBody ChatMessageReq chatMessageReq) {
        Long uid = RequestHolderDTO.get().getUid();
        return chatService.send(uid, chatMessageReq);
    }

    /**
     * 获取消息列表接口
     *
     * @param request 消息请求
     * @return 消息列表
     */
    @GetMapping("/message/list")
    @Operation(summary = "消息列表", description = "消息列表")
    @FrequencyControl(time = 120, count = 20, target = FrequencyControl.Target.IP)
    public ResultUtil<CursorPageBaseResp<ChatMessageResp>> listMessage(@Valid ChatMessageCursorReq request) {
        Long uid = RequestHolderDTO.get().getUid();
        CursorPageBaseResp<ChatMessageResp> messageList = chatService.listMessage(uid, request);
        return ResultUtil.success(messageList);
    }

    /**
     * 获取群成员信息
     */
    @GetMapping("member/list")
    public ResultUtil<CursorPageBaseResp<ChatMemberResp>> listMember(@Valid ChatMemberCursorReq chatMemberCursorReq) {
        Long uid = RequestHolderDTO.get().getUid();
        CursorPageBaseResp<ChatMemberResp> chatMemberRespCursorPageBaseResp = chatService.listMember(uid, chatMemberCursorReq);
        return ResultUtil.success(chatMemberRespCursorPageBaseResp);
    }
}
