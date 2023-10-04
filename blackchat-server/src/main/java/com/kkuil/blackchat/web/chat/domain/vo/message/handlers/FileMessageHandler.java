package com.kkuil.blackchat.web.chat.domain.vo.message.handlers;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.kkuil.blackchat.dao.MessageDAO;
import com.kkuil.blackchat.domain.entity.Message;
import com.kkuil.blackchat.web.chat.domain.enums.MessageTypeEnum;
import com.kkuil.blackchat.web.chat.domain.vo.request.message.MessageExtra;
import com.kkuil.blackchat.web.chat.domain.vo.request.message.body.FileMessageReqBody;
import com.kkuil.blackchat.web.chat.domain.vo.request.message.body.ImageMessageReqBody;
import com.kkuil.blackchat.web.chat.domain.vo.response.message.ChatMessageResp;
import com.kkuil.blackchat.web.chat.domain.vo.response.message.body.FileMessageRespBody;
import com.kkuil.blackchat.web.chat.domain.vo.response.message.body.ImageMessageRespBody;
import com.kkuil.blackchat.web.websocket.domain.vo.request.ChatMessageReq;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author Kkuil
 * @Date 2023/10/4 16:43
 * @Description 文件处理器
 */
@Service
public class FileMessageHandler extends AbstractMessageHandler<FileMessageRespBody> {

    @Resource
    private MessageDAO messageDao;

    /**
     * 消息类型
     *
     * @return 消息类型
     */
    @Override
    MessageTypeEnum getMessageTypeEnum() {
        return MessageTypeEnum.FILE;
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
        FileMessageReqBody fileMessageReqBody = BeanUtil.toBean(body, FileMessageReqBody.class);
        messageExtra.setFileMsg(fileMessageReqBody);
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
        FileMessageRespBody fileMessageRespBody = this.buildResponseBody(message);
        return builder.body(fileMessageRespBody).build();
    }

    /**
     * 构建消息返回体对象
     *
     * @param message 消息体对象
     * @return 消息体对象
     */
    @Override
    public FileMessageRespBody buildResponseBody(Message message) {
        FileMessageRespBody imageMessageRespBody = new FileMessageRespBody();
        FileMessageReqBody imageMessageReqBody = message.getExtra().getFileMsg();

        if (ObjectUtil.isNotNull(imageMessageReqBody)) {
            imageMessageRespBody.setSize(imageMessageReqBody.getSize());
            imageMessageRespBody.setFileName(imageMessageReqBody.getFileName());
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
        return "[文件]";
    }

    /**
     * 会话列表——展示的消息
     *
     * @param message 消息体
     * @return 会话列表——展示的消息
     */
    @Override
    public String showInContactMessage(Message message) {
        return "[文件]";
    }
}
