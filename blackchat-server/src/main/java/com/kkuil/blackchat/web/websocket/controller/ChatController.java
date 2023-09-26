package com.kkuil.blackchat.web.websocket.controller;

import com.kkuil.blackchat.utils.ResultUtil;
import com.kkuil.blackchat.web.websocket.domain.vo.request.ChatMessageReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author Kkuil
 * @Date 2023/9/26 14:47
 * @Description 聊天控制器
 */
@RestController
@Slf4j
@RequestMapping("/chat")
public class ChatController {

    /**
     * 发送消息接口
     *
     * @param chatMessageReq 发送消息体
     * @return 响应体
     */
    @PostMapping("/send")
    @Operation(summary = "发送消息", description = "发送消息")
    @Parameter(name = "chatMessageReq", description = "聊天请求消息")
    public ResultUtil<?> send(@Valid @RequestBody ChatMessageReq chatMessageReq) {
        return null;
    }

}
