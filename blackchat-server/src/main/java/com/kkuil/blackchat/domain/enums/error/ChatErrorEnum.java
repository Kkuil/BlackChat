package com.kkuil.blackchat.domain.enums.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author Kkuil
 * @Date 2023/9/28 16:32
 * @Description 聊天异常枚举
 */
@AllArgsConstructor
@Getter
public enum ChatErrorEnum implements ErrorEnum {
    /**
     *
     */
    ROOM_NOT_EXIST(1002, "房间不存在"),
    ROOM_IS_FULL(1003, "房间已满"),
    NOT_FRIEND(1004, "对方不是您的好友，请先加对方为好友吧~"),
    NOT_IN_GROUP(1005, "您不在该群聊中，请先加入该群在进行操作吧~"),
    MESSAGE_NOT_EXIST(1006, "消息不存在"),
    REPLY_MESSAGE_NOT_MATCH(1006, "发送消息和回复的消息不在一个会话内"),
    AT_USER_REPEAT(1007, "重复艾特同一名用户"),
    AT_USER_NOT_EXIST(1008, "艾特用户不存在"),
    PEOPLE_COUNT_NOT_MATCH(1008, "单聊人数不匹配"),
    NO_AUTH(1008, "暂无此权限"),
    NOT_GROUP(1009, "该房间不是群聊"),
    TEMP_USER_NOT_ALLOWED(1010, "临时用户无法执行该操作，请先登录吧~"),
    ;

    private final Integer code;
    private final String msg;

    @Override
    public Integer getErrorCode() {
        return code;
    }

    @Override
    public String getErrorMsg() {
        return msg;
    }
}
