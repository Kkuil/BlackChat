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
    REPLY_MESSAGE_NOT_MATCH(1007, "发送消息和回复的消息不在一个会话内"),
    AT_USER_REPEAT(1008, "重复艾特同一名用户"),
    AT_USER_NOT_EXIST(1008, "艾特用户不存在"),
    PEOPLE_COUNT_NOT_MATCH(1009, "单聊人数不匹配"),
    NO_AUTH(1010, "暂无此权限"),
    NOT_GROUP(1011, "该房间不是群聊"),
    TEMP_USER_NOT_ALLOWED(1012, "临时用户无法执行该操作，请先登录吧~"),
    NOT_ALLOWED_TO_EXIT_ALL_GROUP(1013, "无法退出全员群~"),
    NOT_ALLOWED_COUNT_GROUP(1014, "群聊人数不匹配~"),
    SENSITIVE_WORD(1015, "消息内容包含敏感词，请文明发言~"),
    CREATE_GROUP_MAX_COUNT(1016, "建群数量已达上限~"),
    REPEAT_OPERATED(1017, "消息已被操作过了，无法二次操作~"),
    MAX_COUNT_PER_GROUP_LIMIT(1018, "群人数超过上限，请删除后再进行操作吧~"),
    NOT_ALLOWED_REVOKE(1019, "您无权撤回该消息~"),
    NO_POWER_FOR_INVITE(1020, "您暂时还无权邀请朋友加群，请联系管理员后再试吧~"),
    INVITE_GROUP_FAILED(1021, "发出邀请失败，请稍后再试~"),
    EXIST_GROUP_MEMBER(1022, "您已是该群成员，操作失败~"),
    EMPTY_LIST(1023, "空列表"),
    NOT_ALLOWED_TO_REVOKE_MESSAGE_OVER_THREE_MINUTES(1024, "消息已发送超过三分钟，不允许撤回~"),
    NO_POWER_FOR_ADD_ADMIN(1025, "您不是群主，无法进行添加管理员操作~"),
    MAX_ADMIN_COUNT_LIMIT(1026, "最多添加三名管理员~"),
    NOT_ALLOWED_ADD_ADMIN_WITH_NOT_MEMBER(1027, "不允许添加非本群成员的人作为管理员"),
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
