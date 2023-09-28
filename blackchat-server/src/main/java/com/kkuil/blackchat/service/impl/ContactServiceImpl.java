package com.kkuil.blackchat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.Contact;
import com.kkuil.blackchat.service.ContactService;
import com.kkuil.blackchat.mapper.ContactMapper;
import org.springframework.stereotype.Service;

/**
* @author 小K
* @description 针对表【contact(会话列表)】的数据库操作Service实现
* @createDate 2023-09-28 14:45:37
*/
@Service
public class ContactServiceImpl extends ServiceImpl<ContactMapper, Contact>
    implements ContactService{

}
