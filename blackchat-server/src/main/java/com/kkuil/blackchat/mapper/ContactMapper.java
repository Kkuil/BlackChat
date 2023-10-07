package com.kkuil.blackchat.mapper;

import com.kkuil.blackchat.core.contact.domain.vo.request.ChatContactCursorReq;
import com.kkuil.blackchat.domain.bo.contact.ContactWithActiveMsg;
import com.kkuil.blackchat.domain.entity.Contact;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kkuil.blackchat.domain.vo.response.CursorPageBaseResp;
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
}
