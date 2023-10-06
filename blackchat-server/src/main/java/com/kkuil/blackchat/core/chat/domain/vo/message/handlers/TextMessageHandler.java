package com.kkuil.blackchat.core.chat.domain.vo.message.handlers;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.kkuil.blackchat.dao.*;
import com.kkuil.blackchat.domain.entity.Message;
import com.kkuil.blackchat.domain.entity.Room;
import com.kkuil.blackchat.domain.enums.error.ChatErrorEnum;
import com.kkuil.blackchat.domain.enums.error.CommonErrorEnum;
import com.kkuil.blackchat.service.RoomService;
import com.kkuil.blackchat.service.impl.ChatGroupSpecialMemberEnum;
import com.kkuil.blackchat.utils.AssertUtil;
import com.kkuil.blackchat.utils.discover.PrioritizedUrlDiscover;
import com.kkuil.blackchat.utils.discover.domain.UrlInfo;
import com.kkuil.blackchat.core.chat.domain.enums.GroupRoleEnum;
import com.kkuil.blackchat.core.chat.domain.enums.MessageTypeEnum;
import com.kkuil.blackchat.core.chat.domain.enums.RoomTypeEnum;
import com.kkuil.blackchat.core.chat.domain.vo.request.message.MessageExtra;
import com.kkuil.blackchat.core.chat.domain.vo.request.message.body.TextMessageReqBody;
import com.kkuil.blackchat.core.chat.domain.vo.response.message.body.TextMessageRespBody;
import com.kkuil.blackchat.core.websocket.domain.vo.request.ChatMessageReq;
import com.kkuil.blackchat.core.chat.domain.vo.response.message.ChatMessageResp;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author Kkuil
 * @Date 2023/9/28 10:18
 * @Description 文本消息处理器
 */
@Service
public class TextMessageHandler extends AbstractMessageHandler<TextMessageRespBody> {

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

    private static final PrioritizedUrlDiscover URL_TITLE_DISCOVER = new PrioritizedUrlDiscover();

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
    public void checkMessage(ChatMessageReq chatMessageReq, Long uid) {
        // 1. 将消息体转换为文本消息体
        Object body = chatMessageReq.getBody();
        Long roomId = chatMessageReq.getRoomId();
        TextMessageReqBody textMessage = BeanUtil.toBean(body, TextMessageReqBody.class);
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
            if (isContainAll && room.isRoomGroup()) {
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
    public void saveMessage(Message message, ChatMessageReq chatMessageReq) {
        // 保存消息内容
        Object body = chatMessageReq.getBody();
        TextMessageReqBody textMessageReq = BeanUtil.toBean(body, TextMessageReqBody.class);
        String content = textMessageReq.getContent();
        Long id = message.getId();

        // 1. 额外消息处理
        MessageExtra extra = Optional.ofNullable(message.getExtra()).orElse(new MessageExtra());
        // 1.1 判断消息url跳转
        Map<String, UrlInfo> urlContentMap = URL_TITLE_DISCOVER.getUrlContentMap(textMessageReq.getContent());
        if (ObjectUtil.isNotNull(urlContentMap)) {
            extra.setUrlContentMap(urlContentMap);
        }
        // 1.2 艾特消息保存
        if (CollectionUtil.isNotEmpty(textMessageReq.getAtUidList())) {
            extra.setAtUidList(textMessageReq.getAtUidList());
        }

        Message update = Message.builder()
                .id(id)
                .content(content)
                .extra(extra)
                .build();
        messageDao.updateById(update);
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
        TextMessageRespBody textMessageRespBody = this.buildResponseBody(message);
        return builder.body(textMessageRespBody).build();
    }

    /**
     * 构建消息返回体对象
     *
     * @param message 消息体对象
     * @return 消息体对象
     */
    @Override
    public TextMessageRespBody buildResponseBody(Message message) {
        MessageExtra extra = Optional.ofNullable(message.getExtra()).orElse(new MessageExtra());
        TextMessageRespBody.TextMessageRespBodyBuilder builder = TextMessageRespBody.builder().content(message.getContent());

        // 1. 判断是否有艾特消息
        if (CollectionUtil.isNotEmpty(extra.getAtUidList())) {
            builder.atUidList(extra.getAtUidList());
        }

        // 2. 判断是否有url小卡片消息
        if (CollectionUtil.isNotEmpty(extra.getUrlContentMap())) {
            builder.urlContentMap(extra.getUrlContentMap());
        }

        return builder.build();
    }

    /**
     * 被回复时——展示的消息
     *
     * @param message 消息体
     * @return 被回复时——展示的消息
     */
    @Override
    public String showInReplyMessage(Message message) {
        return message.getContent();
    }

    /**
     * 会话列表——展示的消息
     *
     * @param message 消息体
     * @return 会话列表——展示的消息
     */
    @Override
    public String showInContactMessage(Message message) {
        return message.getContent();
    }
}
