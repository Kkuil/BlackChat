package com.kkuil.blackchat.cache;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kkuil.blackchat.constant.RedisKeyConst;
import com.kkuil.blackchat.dao.GroupMemberDAO;
import com.kkuil.blackchat.domain.bo.room.GroupBaseInfo;
import com.kkuil.blackchat.domain.entity.GroupMember;
import com.kkuil.blackchat.utils.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/9/27 11:26
 * @Description 群缓存
 */
@Component
public class GroupCache {
}
