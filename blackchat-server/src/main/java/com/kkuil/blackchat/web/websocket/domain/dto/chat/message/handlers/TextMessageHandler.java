package com.kkuil.blackchat.web.websocket.domain.dto.chat.message.handlers;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.kkuil.blackchat.dao.*;
import com.kkuil.blackchat.domain.entity.Message;
import com.kkuil.blackchat.domain.entity.Room;
import com.kkuil.blackchat.domain.enums.error.ChatErrorEnum;
import com.kkuil.blackchat.service.RoomService;
import com.kkuil.blackchat.service.impl.ChatGroupSpecialMemberEnum;
import com.kkuil.blackchat.utils.AssertUtil;
import com.kkuil.blackchat.utils.discover.domain.UrlInfo;
import com.kkuil.blackchat.utils.sensitive.SensitiveWordBs;
import com.kkuil.blackchat.web.chat.domain.enums.GroupRoleEnum;
import com.kkuil.blackchat.web.chat.domain.enums.MessageTypeEnum;
import com.kkuil.blackchat.web.chat.domain.enums.RoomTypeEnum;
import com.kkuil.blackchat.web.chat.domain.vo.request.TextMessageReqVO;
import com.kkuil.blackchat.web.chat.domain.vo.response.TextMessageResp;
import com.kkuil.blackchat.web.websocket.domain.dto.chat.AbstractChatMessageBaseReq;
import com.kkuil.blackchat.web.websocket.domain.vo.request.ChatMessageReq;
import com.kkuil.blackchat.web.websocket.domain.vo.response.ChatMessageResp;
import jakarta.annotation.Resource;

import java.util.*;

/**
 * @Author Kkuil
 * @Date 2023/9/28 10:18
 * @Description 文本消息处理器
 */
@Resource
public class TextMessageHandler extends AbstractMessageHandler {

    @Resource
    private SensitiveWordBs sensitiveWordBs;

    @Resource
    private MessageDAO messageDao;

    @Resource
    private UserDAO userDao;

    @Resource
    private RoomService roomService;

    @Resource
    private GroupMemberDAO groupMemberDao;

    @Resource
    private RoomDAO roomDao;

    /**
     * 消息类型
     *
     * @return 消息类型
     */
    @Override
    MessageTypeEnum getMessageTypeEnum() {
        return MessageTypeEnum.TEXT;
    }

    /**
     * 校验消息——保存前校验
     *
     * @param chatMessageReq 请求消息体
     * @param uid            发送消息的用户ID
     */
    @Override
    public void checkMessage(ChatMessageReq<? extends AbstractChatMessageBaseReq> chatMessageReq, Long uid) {
        // 1. 将消息体转换为文本消息体
        AbstractChatMessageBaseReq body = chatMessageReq.getBody();
        Long roomId = chatMessageReq.getRoomId();
        TextMessageReqVO textMessage = BeanUtil.toBean(body, TextMessageReqVO.class);
        // 2. 检查艾特消息
        List<Long> atUidList = textMessage.getAtUidList();
        boolean isAtUidListNotEmpty = CollectionUtil.isNotEmpty(atUidList);
        if (isAtUidListNotEmpty) {
            // 2.1 判断是否有重复艾特
            HashSet<Long> atUidSet = new HashSet<>(atUidList);
            AssertUtil.equal(atUidList.size(), atUidSet.size(), ChatErrorEnum.AT_USER_REPEAT.getMsg());
            // 2.2 判断艾特用户是否存在
            List<Long> notExistList = atUidList.stream().filter(id -> userDao.getById(id) == null).toList();
            AssertUtil.isFalse(notExistList.size() > 0, ChatErrorEnum.AT_USER_NOT_EXIST.getMsg());
            // 2.3 判断艾特的人是否在同一房间内（也就是说判断当前房间内是否存在被艾特的人）
            Long[] atUidArr = ArrayUtil.toArray(atUidList, Long.class);
            Boolean isSameRoom = roomService.checkRoomMembership(roomId, atUidArr);
            AssertUtil.isTrue(isSameRoom, ChatErrorEnum.NOT_IN_GROUP.getMsg());
            // 2.4 判断艾特用户中是否有全体用户
            boolean isContainAll = atUidList.contains(ChatGroupSpecialMemberEnum.ALL.getId());
            Room room = roomDao.getById(roomId);
            Integer roomType = room.getType();
            if (isContainAll && roomType.equals(RoomTypeEnum.GROUP.getType())) {
                // 2.4.1 有且是群聊，则判断是否有权限
                List<Integer> authorities = new ArrayList<>();
                // 这是群中的两个可以艾特全体人员的权限
                authorities.add(GroupRoleEnum.LEADER.getType());
                authorities.add(GroupRoleEnum.MANAGER.getType());
                Boolean isAuth = groupMemberDao.hasAuthority(roomId, uid, authorities);
                AssertUtil.isTrue(isAuth, ChatErrorEnum.NO_AUTH.getMsg());
            }
            // 2.4.2 没有，则直接跳过
        }
    }

    /**
     * 保存消息
     *
     * @param message        消息
     * @param chatMessageReq 请求消息体
     */
    @Override
    public void saveMessage(Message message, ChatMessageReq<? extends AbstractChatMessageBaseReq> chatMessageReq) {
        // 保存消息内容
        String content = message.getContent();
        Message update = Message.builder().content(content).build();
        messageDao.updateById(update);
    }

    /**
     * 构建响应消息体
     *
     * @param message 消息对象
     * @return 响应消息体
     */
    @Override
    public ChatMessageResp buildChatMessageResp(Message message) {
        // 1. 获取消息
        Long fromUid = message.getFromUid();
        Long id = message.getId();
        Date createTime = message.getCreateTime();
        Integer type = message.getType();
        String content = message.getContent();
        // TextMessageResp.ReplyMsg reply = TextMessageResp.ReplyMsg
        //         .builder()
        //         .id()
        //         .uid()
        //         .username()
        //         .type()
        //         .body()
        //         .canCallback()
        //         .gapCount()
        //         .build();
        // 2. 构建消息
        TextMessageResp.ReplyMsg replyMsg = new TextMessageResp.ReplyMsg();
        TextMessageResp textMessageResp = TextMessageResp.builder()
                .content(content)
                .urlContentMap(Collections.singletonMap("url", new UrlInfo()))
                .atUidList(new ArrayList<>())
                .reply(replyMsg)
                .build();
        ChatMessageResp.UserInfo userInfo = ChatMessageResp.UserInfo.builder()
                .uid(fromUid)
                .build();
        ChatMessageResp.Message msg = ChatMessageResp.Message.builder()
                .id(id)
                .sendTime(createTime)
                .type(type)
                .body(textMessageResp)
                .build();
        return ChatMessageResp.builder()
                .fromUser(userInfo)
                .message(msg)
                .build();
    }

    /**
     * 被回复时——展示的消息
     *
     * @param message 消息体
     * @return 被回复时——展示的消息
     */
    @Override
    public Object showReplyMessage(Message message) {
        return message.getFromUid() + ": " + message.getContent();
    }

    /**
     * 会话列表——展示的消息
     *
     * @param message 消息体
     * @return 会话列表——展示的消息
     */
    @Override
    public String showContactMessage(Message message) {
        return null;
    }
}
