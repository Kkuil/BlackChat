package com.kkuil.blackchat.core.chat.domain.vo.message.handlers;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.kkuil.blackchat.dao.MessageDAO;
import com.kkuil.blackchat.domain.entity.Message;
import com.kkuil.blackchat.core.chat.domain.enums.MessageTypeEnum;
import com.kkuil.blackchat.core.chat.domain.vo.request.message.MessageExtra;
import com.kkuil.blackchat.core.chat.domain.vo.request.message.body.ImageMessageReqBody;
import com.kkuil.blackchat.core.chat.domain.vo.response.message.ChatMessageResp;
import com.kkuil.blackchat.core.chat.domain.vo.response.message.body.ImageMessageRespBody;
import com.kkuil.blackchat.core.websocket.domain.vo.request.ChatMessageReq;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author Kkuil
 * @Date 2023/10/4 16:43
 * @Description 图片处理器
 */
@Service
public class ImageMessageHandler extends AbstractMessageHandler<ImageMessageRespBody> {

    @Resource
    private MessageDAO messageDao;

    /**
     * 消息类型
     *
     * @return 消息类型
     */
    @Override
    MessageTypeEnum getMessageTypeEnum() {
        return MessageTypeEnum.IMG;
    }

    /**
     * 校验消息——保存前校验
     *
     * @param chatMessageReq 请求消息体
     * @param uid            发送消息的用户ID
     */
    @Override
    public void checkMessage(ChatMessageReq chatMessageReq, Long uid) {
        // TODO 这个地方可以使用AI进行鉴黄处理  咳咳咳咳咳咳咳
        return;
    }

    /**
     * 保存消息
     *
     * @param message        消息
     * @param chatMessageReq 请求消息体
     */
    @Override
    public void saveMessage(Message message, ChatMessageReq chatMessageReq) {
        MessageExtra messageExtra = new MessageExtra();
        Object body = chatMessageReq.getBody();
        ImageMessageReqBody imageMessageReqBody = BeanUtil.toBean(body, ImageMessageReqBody.class);
        messageExtra.setImageMessageReqBody(imageMessageReqBody);
        message.setExtra(messageExtra);
        messageDao.updateById(message);
    }

    /**
     * 构建响应消息体
     *
     * @param message 消息对象
     * @param builder 构造器
     * @return 响应消息体
     */
    @Override
    public ChatMessageResp.Message buildChatMessageResp(Message message, ChatMessageResp.Message.MessageBuilder builder) {
        ImageMessageRespBody imageMessageReqBody = this.buildResponseBody(message);
        return builder.body(imageMessageReqBody).build();
    }

    /**
     * 构建消息返回体对象
     *
     * @param message 消息体对象
     * @return 消息体对象
     */
    @Override
    public ImageMessageRespBody buildResponseBody(Message message) {
        ImageMessageRespBody imageMessageRespBody = new ImageMessageRespBody();
        ImageMessageReqBody imageMessageReqBody = message.getExtra().getImageMessageReqBody();

        if (ObjectUtil.isNotNull(imageMessageReqBody)) {
            imageMessageRespBody.setSize(imageMessageReqBody.getSize());
            imageMessageRespBody.setWidth(imageMessageReqBody.getWidth());
            imageMessageRespBody.setHeight(imageMessageReqBody.getHeight());
            imageMessageRespBody.setUrl(imageMessageReqBody.getUrl());
        }
        return imageMessageRespBody;
    }

    /**
     * 被回复时——展示的消息
     *
     * @param message 消息体
     * @return 被回复时——展示的消息
     */
    @Override
    public String showInReplyMessage(Message message) {
        return "[图片]";
    }

    /**
     * 会话列表——展示的消息
     *
     * @param message 消息体
     * @return 会话列表——展示的消息
     */
    @Override
    public String showInContactMessage(Message message) {
        return "[图片]";
    }
}
