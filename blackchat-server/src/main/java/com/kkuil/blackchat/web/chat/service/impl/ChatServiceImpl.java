package com.kkuil.blackchat.web.chat.service.impl;

import com.kkuil.blackchat.dao.MessageDAO;
import com.kkuil.blackchat.domain.entity.Message;
import com.kkuil.blackchat.domain.enums.error.ChatErrorEnum;
import com.kkuil.blackchat.domain.vo.response.CursorPageBaseResp;
import com.kkuil.blackchat.service.MessageService;
import com.kkuil.blackchat.service.RoomService;
import com.kkuil.blackchat.utils.AssertUtil;
import com.kkuil.blackchat.utils.ResultUtil;
import com.kkuil.blackchat.web.chat.domain.vo.request.MemberReq;
import com.kkuil.blackchat.web.chat.domain.vo.response.ChatMemberResp;
import com.kkuil.blackchat.web.websocket.domain.vo.request.ChatMessageReq;
import com.kkuil.blackchat.web.chat.domain.vo.response.ChatMessageResp;
import com.kkuil.blackchat.web.chat.service.ChatService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author Kkuil
 * @Date 2023/9/28 9:52
 * @Description 聊天业务类
 */
@Service
public class ChatServiceImpl implements ChatService {

    @Resource
    private RoomService roomService;

    @Resource
    private MessageService messageService;

    @Resource
    private MessageDAO messageDao;

    /**
     * 发送消息
     *
     * @param chatMessageReq 消息体
     * @return 响应体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultUtil<ChatMessageResp> send(Long uid, ChatMessageReq chatMessageReq) {
        // 1. 检查用户与房间的关系
        roomService.check(uid, chatMessageReq);

        // 2. 检查用户与其发的消息的情况
        messageService.check(uid, chatMessageReq);

        // 3. 保存消息(除了与回复消息间隔数)
        Message message = messageDao.saveByUidAndChatMessageReq(uid, chatMessageReq);

        // 4. 保存与回复消息间隔数
        Long roomId = chatMessageReq.getRoomId();
        Long messageId = message.getId();
        Long replyMessageId = chatMessageReq.getReplyMessageId();
        messageDao.saveGapCount(roomId, messageId, replyMessageId);

        // 5. 让各自的消息处理器保存消息
        messageService.save(message, chatMessageReq);

        // 6. 构建消息响应体
        ChatMessageResp chatMessageResp = messageService.buildChatMessageResp(messageId, chatMessageReq);
        return ResultUtil.success(chatMessageResp);
    }

    /**
     * 获取成员信息
     *
     * @param uid       用户ID
     * @param memberReq 成员请求信息
     * @return 成员信息
     */
    @Override
    public CursorPageBaseResp<ChatMemberResp> listMember(Long uid, MemberReq memberReq) {
        // 1. 检查用户是否在房间内
        Long roomId = memberReq.getRoomId();
        Boolean isMember = roomService.checkRoomMembership(roomId, uid);
        AssertUtil.isTrue(isMember, ChatErrorEnum.NOT_IN_GROUP.getMsg());
        return null;
    }
}
