package com.kkuil.blackchat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.SensitiveWord;
import com.kkuil.blackchat.mapper.SensitiveWordMapper;
import org.springframework.stereotype.Service;

/**
 * @Author Kkuil
 * @Date 2023/9/28 17:12
 * @Description 敏感词访问层
 */
@Service
public class SensitiveWordDAO extends ServiceImpl<SensitiveWordMapper, SensitiveWord> {
}
