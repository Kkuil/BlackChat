package com.kkuil.blackchat.dao;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.cache.UserCache;
import com.kkuil.blackchat.constant.MessageConst;
import com.kkuil.blackchat.core.chat.domain.enums.MessageTypeEnum;
import com.kkuil.blackchat.core.user.domain.vo.request.UserInfoCache;
import com.kkuil.blackchat.domain.dto.UserBaseInfo;
import com.kkuil.blackchat.domain.entity.Message;
import com.kkuil.blackchat.domain.enums.RoleEnum;
import com.kkuil.blackchat.domain.vo.response.CursorPageBaseResp;
import com.kkuil.blackchat.mapper.MessageMapper;
import com.kkuil.blackchat.utils.CursorUtil;
import com.kkuil.blackchat.core.chat.domain.enums.MessageStatusEnum;
import com.kkuil.blackchat.core.chat.domain.vo.request.message.ChatMessageCursorReq;
import com.kkuil.blackchat.core.websocket.domain.vo.request.ChatMessageReq;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/9/28 18:28
 * @Description 消息访问层
 */
@Service
public class MessageDAO extends ServiceImpl<MessageMapper, Message> {

    /**
     * 首条建群消息模板字符串
     */
    public static final String FIRST_MESSAGE_FOR_CREATE_GROUP = "欢迎来到%s，开始愉快的聊天吧~";

    /**
     * 首条加好友消息模板字符串
     */
    public static final String FIRST_MESSAGE_FOR_ADD_FRIEND = "%s,%s你们已经是好友了，开始聊天吧~";

    @Resource
    private UserCache userCache;

    /**
     * 通过用户ID和chatMessageReq保存消息
     *
     * @param uid            用户ID
     * @param chatMessageReq 消息体
     * @return 消息
     */
    public Message saveByUidAndChatMessageReq(Long uid, ChatMessageReq chatMessageReq) {
        Message message = Message.builder()
                .fromUid(uid)
                .roomId(chatMessageReq.getRoomId())
                .type(chatMessageReq.getMessageType())
                .replyMessageId(chatMessageReq.getReplyMessageId())
                .status(MessageStatusEnum.NORMAL.getStatus())
                .build();
        this.save(message);
        return this.getById(message.getId());
    }

    /**
     * 计算同一房间内两条消息的间隔数
     *
     * @param roomId 房间号
     * @param fromId 原消息
     * @param toId   定位消息
     */
    public void saveGapCount(Long roomId, Long fromId, Long toId) {
        Long gapCount = lambdaQuery()
                .eq(Message::getRoomId, roomId)
                .gt(Message::getId, fromId)
                .le(Message::getId, toId)
                .count();
        UpdateWrapper<Message> wrapper = new UpdateWrapper<>();
        wrapper
                .lambda()
                .eq(true, Message::getId, toId)
                .set(true, Message::getGapCount, gapCount);
        this.update(wrapper);
    }

    /**
     * 获取消息列表
     *
     * @param request 消息请求
     * @return 消息列表
     */
    public CursorPageBaseResp<Message, String> getCursorPage(ChatMessageCursorReq request) {
        return CursorUtil.getCursorPageByMysql(this, request, wrapper -> {
            wrapper.eq(Message::getStatus, MessageStatusEnum.NORMAL.getStatus());
            wrapper.eq(Message::getRoomId, request.getRoomId());
        }, Message::getCreateTime);
    }

    /**
     * 获取未读数
     *
     * @param roomId   房间ID
     * @param readTime 最后已读时间
     * @return 未读数量
     */
    public Integer getUnReadCountByReadTime(Long roomId, Date readTime) {
        return Math.toIntExact(lambdaQuery()
                .eq(Message::getRoomId, roomId)
                .gt(ObjectUtil.isNotNull(readTime), Message::getCreateTime, readTime)
                .count());
    }

    /**
     * 删除某用户在某个会话中的消息记录
     *
     * @param roomId 房间ID
     * @param uid    用户ID
     * @return 是否删除
     */
    public Boolean delRecordBatch(Long roomId, Long uid) {
        QueryWrapper<Message> wrapper = new QueryWrapper<Message>()
                .eq("room_id", roomId)
                .eq("uid", uid);
        return this.remove(wrapper);
    }

    /**
     * 删除聊天记录
     *
     * @param roomId 房间ID
     */
    public void deleteByRoomId(Long roomId) {
        LambdaQueryWrapper<Message> wrapper = new QueryWrapper<Message>()
                .lambda()
                .eq(Message::getRoomId, roomId);
        this.remove(wrapper);
    }

    /**
     * 判断是否有权利撤回消息
     *
     * @param baseInfo 用户
     * @param id       消息ID
     * @return 是否有权利
     */
    public Boolean hasPower(UserBaseInfo baseInfo, Long id) {
        Message message = this.lambdaQuery()
                .eq(Message::getId, id)
                .one();
        if (baseInfo.getId().equals(message.getFromUid())) {
            return true;
        }
        return ObjectUtil.isNotNull(RoleEnum.of(baseInfo.getRoleId()));
    }

    /**
     * 撤回消息
     *
     * @param baseInfo 撤回消息的用户
     * @param id       消息ID
     */
    public Message revoke(UserBaseInfo baseInfo, Long id) {

        Message message = new Message();
        message.setId(id);
        message.setFromUid(baseInfo.getId());
        message.setContent(String.format(MessageConst.REVOKE_TEXT, baseInfo.getName()));
        message.setReplyMessageId(null);
        message.setGapCount(null);
        message.setType(MessageTypeEnum.REVOKE.getType());
        message.setExtra(null);
        message.setUpdateTime(new Date());

        this.updateById(message);

        return this.lambdaQuery().eq(Message::getId, id).one();
    }

    /**
     * 创建首条建群消息
     *
     * @param uid       用户ID
     * @param roomId    房间ID
     * @param groupName 群名
     * @return 消息对象
     */
    public Message createFristCreateGroupMessage(Long uid, Long roomId, String groupName) {
        Message message = new Message();
        message.setRoomId(roomId);
        message.setFromUid(uid);
        message.setContent(String.format(FIRST_MESSAGE_FOR_CREATE_GROUP, groupName));
        message.setStatus(MessageStatusEnum.NORMAL.getStatus());
        message.setType(MessageTypeEnum.TEXT.getType());
        message.setCreateTime(new Date());
        message.setUpdateTime(new Date());
        this.save(message);
        return message;
    }

    /**
     * 创建首条建立好友关系消息
     *
     * @param roomId   房间ID
     * @param uid      用户ID
     * @param targetId 目标用户ID
     * @return 消息对象
     */
    public Message createFristAddFriendMessage(Long roomId, Long uid, Long targetId) {
        Message message = new Message();
        message.setRoomId(roomId);
        message.setFromUid(targetId);
        List<UserInfoCache> userList = userCache.getBatchByUidList(Arrays.asList(uid, targetId));
        message.setContent(String.format(FIRST_MESSAGE_FOR_ADD_FRIEND, userList.get(0).getName(), userList.get(1).getName()));
        message.setStatus(MessageStatusEnum.NORMAL.getStatus());
        message.setType(MessageTypeEnum.TEXT.getType());
        this.save(message);
        return message;
    }
}
