package com.kkuil.blackchat.utils.sensitive.impl;

import com.kkuil.blackchat.dao.SensitiveWordDAO;
import com.kkuil.blackchat.domain.entity.SensitiveWord;
import com.kkuil.blackchat.utils.sensitive.IWordDeny;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Kkuil
 * @Date 2023/9/28 17:14
 * @Description
 */
@Component
public class MyWordDeny implements IWordDeny {
    @Resource
    private SensitiveWordDAO sensitiveWordDao;

    @Override
    public List<String> deny() {
        return sensitiveWordDao.list()
                .stream()
                .map(SensitiveWord::getWord)
                .collect(Collectors.toList());
    }
}
