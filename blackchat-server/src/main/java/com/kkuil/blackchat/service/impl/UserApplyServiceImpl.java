package com.kkuil.blackchat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkuil.blackchat.domain.entity.UserApply;
import com.kkuil.blackchat.service.UserApplyService;
import com.kkuil.blackchat.mapper.UserApplyMapper;
import org.springframework.stereotype.Service;

/**
* @author 小K
* @description 针对表【user_apply(用户申请表)】的数据库操作Service实现
* @createDate 2023-09-28 14:48:12
*/
@Service
public class UserApplyServiceImpl extends ServiceImpl<UserApplyMapper, UserApply>
    implements UserApplyService {

}
