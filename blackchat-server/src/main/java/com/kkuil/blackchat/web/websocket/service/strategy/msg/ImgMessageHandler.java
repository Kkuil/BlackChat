package com.kkuil.blackchat.web.websocket.service.strategy.msg;

import cn.hutool.core.bean.BeanUtil;
import com.kkuil.blackchat.utils.AssertUtil;
import com.kkuil.blackchat.web.websocket.domain.entity.Message;
import com.kkuil.blackchat.web.websocket.domain.dto.msg.ImgMessageDTO;
import com.kkuil.blackchat.web.websocket.domain.dto.msg.MessageExtra;
import com.kkuil.blackchat.web.websocket.domain.enums.msg.MessageTypeEnum;
import com.kkuil.blackchat.web.websocket.domain.vo.request.ChatMessageReq;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description 图片消息
 */
@Component
public class ImgMessageHandler extends AbstractMessageHandler {

    @Override
    MessageTypeEnum getMessageTypeEnum() {
        return MessageTypeEnum.IMG;
    }

    @Override
    public void checkMessage(ChatMessageReq request, Long uid) {
        ImgMessageDTO body = BeanUtil.toBean(request.getBody(), ImgMessageDTO.class);
        AssertUtil.allCheckValidateThrow(body);
    }

    @Override
    public void saveMessage(Message msg, ChatMessageReq request) {
        ImgMessageDTO body = BeanUtil.toBean(request.getBody(), ImgMessageDTO.class);
        MessageExtra extra = Optional.ofNullable(msg.getExtra()).orElse(new MessageExtra());
        Message update = new Message();
        update.setId(msg.getId());
        update.setExtra(extra);
        extra.setImgMessageDTO(body);
        // TODO 保存信息
        // ......
    }

    @Override
    public Object showMessage(Message msg) {
        return msg.getExtra().getImgMessageDTO();
    }

    @Override
    public Object showReplyMessage(Message msg) {
        return "图片";
    }

    @Override
    public String showContactMessage(Message msg) {
        return "[图片]";
    }
}
