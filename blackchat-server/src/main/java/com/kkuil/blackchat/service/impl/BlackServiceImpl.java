package com.kkuil.blackchat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.Black;
import com.kkuil.blackchat.service.BlackService;
import com.kkuil.blackchat.mapper.BlackMapper;
import org.springframework.stereotype.Service;

/**
* @author 小K
* @description 针对表【black(黑名单)】的数据库操作Service实现
* @createDate 2023-09-27 11:55:28
*/
@Service
public class BlackServiceImpl extends ServiceImpl<BlackMapper, Black>
    implements BlackService {

}
