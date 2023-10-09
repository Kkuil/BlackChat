package com.kkuil.blackchat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.core.contact.domain.vo.request.ChatContactCursorReq;
import com.kkuil.blackchat.domain.bo.contact.ContactWithActiveMsg;
import com.kkuil.blackchat.domain.entity.Contact;
import com.kkuil.blackchat.domain.vo.response.CursorPageBaseResp;
import com.kkuil.blackchat.mapper.ContactMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

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
    public CursorPageBaseResp<ContactWithActiveMsg> getCursorPage(Long uid, ChatContactCursorReq request) {
        List<ContactWithActiveMsg> list = contactMapper.getCursorPage(uid, request);
        CursorPageBaseResp<ContactWithActiveMsg> contactWithActiveMsgCursorPageBaseResp = new CursorPageBaseResp<>();
        contactWithActiveMsgCursorPageBaseResp.setCursor(String.valueOf(list.get(list.size() - 1).getActiveTime().getTime()));
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
    public Contact getByRoomId(Long uid, Long roomId) {
        return this.lambdaQuery().eq(Contact::getUid, uid).eq(Contact::getRoomId, roomId).one();
    }
}
