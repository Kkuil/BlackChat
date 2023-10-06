package com.kkuil.blackchat.cache;

import cn.hutool.core.util.ObjectUtil;
import com.kkuil.blackchat.constant.RedisKeyConst;
import com.kkuil.blackchat.dao.ContactDAO;
import com.kkuil.blackchat.domain.bo.ContactBaseInfo;
import com.kkuil.blackchat.utils.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/10/5 18:44
 * @Description 会话缓存
 */
@Service
public class ContactCache {

    @Resource
    private ContactDAO contactDao;

    /**
     * 通过用户ID获取会话列表
     *
     * @param uid 用户信息
     * @return 会话列表信息
     */
    public List<ContactBaseInfo> listContactByUid(Long uid) {
        List<ContactBaseInfo> contactBaseInfos = contactDao.listContactByUid(uid);
        return contactBaseInfos;
    }

}
