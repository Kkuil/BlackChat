package com.kkuil.blackchat.dao;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.core.chat.domain.enums.MessageStatusEnum;
import com.kkuil.blackchat.core.contact.domain.vo.request.ChatContactCursorReq;
import com.kkuil.blackchat.core.contact.domain.vo.response.ChatContactCursorResp;
import com.kkuil.blackchat.domain.bo.ContactBaseInfo;
import com.kkuil.blackchat.domain.entity.Contact;
import com.kkuil.blackchat.domain.entity.Message;
import com.kkuil.blackchat.domain.vo.response.CursorPageBaseResp;
import com.kkuil.blackchat.mapper.ContactMapper;
import com.kkuil.blackchat.utils.CursorUtil;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author Kkuil
 * @Date 2023/10/5 18:53
 * @Description 会话访问层
 */
@Service
public class ContactDAO extends ServiceImpl<ContactMapper, Contact> {

    /***
     * 通过用户ID获取房间ID
     * @param uid 用户ID
     * @return 房间列表
     */
    public List<ContactBaseInfo> listContactByUid(Long uid) {
        List<Contact> list = lambdaQuery()
                .eq(Contact::getUid, uid)
                .select(Contact::getRoomId, Contact::getActiveTime, Contact::getLastMsgId, Contact::getReadTime)
                .list();
        return list.stream().map(contact -> {
            ContactBaseInfo contactBaseInfo = new ContactBaseInfo();
            contactBaseInfo.setRoomId(contact.getRoomId());
            contactBaseInfo.setReadTime(contact.getReadTime());
            contactBaseInfo.setActiveTime(contact.getActiveTime());
            contactBaseInfo.setLastMsgId(contact.getLastMsgId());
            return contactBaseInfo;
        }).toList();
    }

    /**
     * 游标获取数据
     *
     * @param list 数据
     * @return 数据
     */
    public CursorPageBaseResp<Contact> getCursorPage(Long uid, ChatContactCursorReq request) {
        return CursorUtil.getCursorPageByMysql(this, request, wrapper -> {
            wrapper.eq(Contact::getUid, uid);
        }, Contact::getActiveTime);
    }

    /**
     * 更新会话的最新消息
     *
     * @param roomId 房间ID
     * @param date   最新时间
     * @param msgId  消息ID
     */
    public void updateNewestMessage(Long roomId, Date date, Long msgId) {
        this.lambdaUpdate()
                .eq(Contact::getRoomId, roomId)
                .set(ObjectUtil.isNotNull(date), Contact::getActiveTime, date)
                .set(ObjectUtil.isNotNull(msgId), Contact::getLastMsgId, msgId);
    }
}
