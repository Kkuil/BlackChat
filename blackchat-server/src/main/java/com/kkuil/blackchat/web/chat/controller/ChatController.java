package com.kkuil.blackchat.web.chat.controller;

import com.kkuil.blackchat.anotations.FrequencyControl;
import com.kkuil.blackchat.domain.dto.RequestHolderDTO;
import com.kkuil.blackchat.web.websocket.domain.dto.chat.AbstractChatMessageBaseReq;
import com.kkuil.blackchat.utils.ResultUtil;
import com.kkuil.blackchat.web.websocket.domain.vo.request.ChatMessageReq;
import com.kkuil.blackchat.web.websocket.domain.vo.response.ChatMessageResp;
import com.kkuil.blackchat.web.websocket.service.ChatService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResultUtil<ChatMessageResp> send(@Valid @RequestBody ChatMessageReq<? extends AbstractChatMessageBaseReq> chatMessageReq) {
        Long uid = RequestHolderDTO.get().getUid();
        return chatService.send(uid, chatMessageReq);
    }
}
