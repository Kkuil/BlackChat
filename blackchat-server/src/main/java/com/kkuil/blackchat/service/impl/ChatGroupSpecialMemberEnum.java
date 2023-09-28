package com.kkuil.blackchat.service.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author Kkuil
 * @Date 2023/9/28 20:16
 * @Description 群聊中特殊的成员
 */
@Getter
@AllArgsConstructor
public enum ChatGroupSpecialMemberEnum {

    /**
     * 全体用户
     */
    ALL(0L),
    ;

    private final Long id;

}
