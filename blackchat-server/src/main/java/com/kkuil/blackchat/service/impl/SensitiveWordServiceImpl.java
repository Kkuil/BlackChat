package com.kkuil.blackchat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.SensitiveWord;
import com.kkuil.blackchat.service.ISensitiveWordService;
import com.kkuil.blackchat.mapper.SensitiveWordMapper;
import org.springframework.stereotype.Service;

/**
* @author 小K
* @description 针对表【sensitive_word(敏感词库)】的数据库操作Service实现
* @createDate 2023-09-27 11:55:56
*/
@Service
public class SensitiveWordServiceImpl extends ServiceImpl<SensitiveWordMapper, SensitiveWord>
    implements ISensitiveWordService {

}
