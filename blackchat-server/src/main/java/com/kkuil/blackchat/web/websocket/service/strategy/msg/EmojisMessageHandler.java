package com.kkuil.blackchat.web.websocket.service.strategy.msg;

import cn.hutool.core.bean.BeanUtil;
import com.kkuil.blackchat.utils.AssertUtil;
import com.kkuil.blackchat.web.websocket.domain.entity.Message;
import com.kkuil.blackchat.web.websocket.domain.dto.msg.EmojisMessageDTO;
import com.kkuil.blackchat.web.websocket.domain.dto.msg.MessageExtra;
import com.kkuil.blackchat.web.websocket.domain.enums.msg.MessageTypeEnum;
import com.kkuil.blackchat.web.websocket.domain.vo.request.ChatMessageReq;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description 表情消息
 */
@Component
public class EmojisMessageHandler extends AbstractMessageHandler {

    @Override
    MessageTypeEnum getMessageTypeEnum() {
        return MessageTypeEnum.EMOJI;
    }

    @Override
    public void checkMessage(ChatMessageReq request, Long uid) {
        EmojisMessageDTO body = BeanUtil.toBean(request.getBody(), EmojisMessageDTO.class);
        AssertUtil.allCheckValidateThrow(body);
    }

    @Override
    public void saveMessage(Message msg, ChatMessageReq request) {
        EmojisMessageDTO body = BeanUtil.toBean(request.getBody(), EmojisMessageDTO.class);
        MessageExtra extra = Optional.ofNullable(msg.getExtra()).orElse(new MessageExtra());
        Message update = new Message();
        update.setId(msg.getId());
        update.setExtra(extra);
        extra.setEmojisMessageDTO(body);
        // TODO 保存消息
        // ......
    }

    @Override
    public Object showMessage(Message msg) {
        return msg.getExtra().getEmojisMessageDTO();
    }

    @Override
    public Object showReplyMessage(Message msg) {
        return "表情";
    }

    @Override
    public String showContactMessage(Message msg) {
        return "[表情包]";
    }
}
