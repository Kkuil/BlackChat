package com.kkuil.blackchat.mapper;

import cn.hutool.core.date.DateTime;
import com.kkuil.blackchat.core.contact.domain.vo.request.ChatContactCursorReq;
import com.kkuil.blackchat.domain.bo.contact.ContactWithActiveMsg;
import com.kkuil.blackchat.domain.entity.Contact;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/9/29 13:08
 * @Description 针对表【contact(会话列表)】的数据库操作Mapper
 */
@Mapper
public interface ContactMapper extends BaseMapper<Contact> {

    /**
     * 游标查询
     *
     * @param uid     用户ID
     * @param request 信息
     * @return 会话列表
     */
    List<ContactWithActiveMsg> getCursorPage(Long uid, ChatContactCursorReq request);

    /**
     * 构建响应体
     *
     * @param uid     用户ID
     * @param contactId 会话ID
     * @return 响应体
     */
    ContactWithActiveMsg getContactWithActiveMsg(Long uid, Long contactId);
}
