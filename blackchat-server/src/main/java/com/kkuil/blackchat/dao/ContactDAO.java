package com.kkuil.blackchat.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.core.contact.domain.vo.request.ChatContactCursorReq;
import com.kkuil.blackchat.domain.bo.contact.ContactWithActiveMsg;
import com.kkuil.blackchat.domain.entity.Contact;
import com.kkuil.blackchat.domain.vo.response.CursorPageBaseResp;
import com.kkuil.blackchat.mapper.ContactMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/10/5 18:53
 * @Description 会话访问层
 */
@Service
public class ContactDAO extends ServiceImpl<ContactMapper, Contact> {

    @Resource
    private ContactMapper contactMapper;

    /**
     * 游标获取数据
     *
     * @return 数据
     */
    public CursorPageBaseResp<ContactWithActiveMsg, Date> getCursorPage(Long uid, ChatContactCursorReq request) {
        List<ContactWithActiveMsg> list = contactMapper.getCursorPage(uid, request);
        CursorPageBaseResp<ContactWithActiveMsg, Date> contactWithActiveMsgCursorPageBaseResp = new CursorPageBaseResp<>();
        if (list.size() > 0) {
            contactWithActiveMsgCursorPageBaseResp.setCursor(list.get(list.size() - 1).getActiveTime());
        }
        contactWithActiveMsgCursorPageBaseResp.setList(list);
        contactWithActiveMsgCursorPageBaseResp.setIsLast(request.getPageSize() > list.size());
        return contactWithActiveMsgCursorPageBaseResp;
    }

    /**
     * 通过用户ID和房间ID获取会话信息
     *
     * @param uid    用户ID
     * @param roomId 房间ID
     * @return 会话信息
     */
    public Contact getByUidRoomId(Long uid, Long roomId) {
        return this.lambdaQuery().eq(Contact::getUid, uid).eq(Contact::getRoomId, roomId).one();
    }

    /**
     * 更新会话最新消息
     *
     * @param roomId     房间ID
     * @param createTime 最新时间
     */
    public void updateReadTime(Long roomId, Date createTime) {
        LambdaUpdateWrapper<Contact> updateWrapper = new UpdateWrapper<Contact>()
                .lambda()
                .eq(Contact::getRoomId, roomId)
                .set(Contact::getReadTime, createTime);
        this.update(updateWrapper);
    }

    /**
     * 删除会话记录
     *
     * @param groupId 群ID
     * @param uid     用户ID
     */
    public Boolean delContact(Long groupId, Long uid) {
        QueryWrapper<Contact> wrapper = new QueryWrapper<Contact>()
                .eq("room_id", groupId)
                .eq("uid", uid);
        return this.remove(wrapper);
    }

    /**
     * 删除会话
     *
     * @param roomId 房间ID
     */
    public void deleteByRoomId(Long roomId) {
        LambdaQueryWrapper<Contact> wrapper = new QueryWrapper<Contact>()
                .lambda()
                .eq(Contact::getRoomId, roomId);
        this.remove(wrapper);
    }

    /**
     * 创建会话
     *
     * @param uid    用户ID
     * @param roomId 房间ID
     * @return contact 会话信息
     */
    public Contact createContact(Long uid, Long roomId, Date readTime) {
        Contact contact = new Contact();
        contact.setUid(uid);
        contact.setRoomId(roomId);
        contact.setReadTime(readTime);
        this.save(contact);
        return contact;
    }

    /**
     * 创建多个会话
     *
     * @param roomId  房间ID
     * @param uidList 用户ID列表
     * @param date    读取时间
     */
    public void createContactBatch(Long groupId, List<Long> uidList, Date date) {
        List<Contact> contactList = new ArrayList<>();
        uidList.forEach(uid -> {
            Contact contact = new Contact();
            contact.setUid(uid);
            contact.setRoomId(groupId);
            contact.setReadTime(date);
            contactList.add(contact);
        });
        this.saveBatch(contactList);
    }
}
