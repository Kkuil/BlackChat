package com.kkuil.blackchat.core.mq;

import com.kkuil.blackchat.core.chat.domain.vo.response.message.ChatMessageResp;
import com.kkuil.blackchat.core.mq.constant.MqConstant;
import com.kkuil.blackchat.core.mq.service.PushService;
import com.kkuil.blackchat.core.websocket.adapter.WsAdapter;
import com.kkuil.blackchat.dao.*;
import com.kkuil.blackchat.domain.entity.Message;
import com.kkuil.blackchat.domain.entity.Room;
import com.kkuil.blackchat.domain.entity.RoomFriend;
import com.kkuil.blackchat.event.domain.dto.MsgSendMessageDTO;
import com.kkuil.blackchat.service.MessageService;
import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/10/6 11:20
 * @Description 发送消息更新房间收信箱，并同步给房间成员信箱
 */
@RocketMQMessageListener(consumerGroup = MqConstant.SEND_MSG_GROUP, topic = MqConstant.SEND_MSG_TOPIC)
@Component
public class MessageSendConsumer implements RocketMQListener<MsgSendMessageDTO> {

    @Resource
    private MessageDAO messageDao;

    @Resource
    private RoomDAO roomDao;

    @Resource
    private ContactDAO contactDao;

    @Resource
    private PushService pushService;

    @Resource
    private MessageService messageService;

    @Resource
    private GroupMemberDAO groupMemberDao;

    @Resource
    private RoomFriendDAO roomFriendDao;

    @Override
    public void onMessage(MsgSendMessageDTO dto) {
        Long msgId = dto.getMsgId();
        Message message = messageDao.getById(msgId);
        Long roomId = message.getRoomId();
        Room room = roomDao.getById(roomId);
        ChatMessageResp chatMessageResp = messageService.buildChatMessageResp(msgId);
        // 1. 更新该房间下的所有会话的最后更新时间和最后一条消息
        contactDao.updateNewestMessage(roomId, new Date(), msgId);

        // 2. 判断是否是热点群聊
        if (room.isHotRoom()) {
            // 2.1 热点群聊
            // 推送给所有人消息
            pushService.sendPushMsg(WsAdapter.buildMsgSend(chatMessageResp));
        } else {
            List<Long> uidList;
            if (room.isRoomGroup()) {
                // 2.2 群聊
                uidList = groupMemberDao.getUidListByRoomId(roomId);
            } else {
                // 2.3 单聊
                RoomFriend roomFriend = roomFriendDao.getUidByRoomId(roomId);
                uidList = Arrays.asList(roomFriend.getUid1(), roomFriend.getUid2());
            }
            // 3. 推送房间成员
            pushService.sendPushMsg(WsAdapter.buildMsgSend(chatMessageResp), uidList);
        }
    }

}
