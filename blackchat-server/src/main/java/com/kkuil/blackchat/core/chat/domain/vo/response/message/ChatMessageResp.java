package com.kkuil.blackchat.core.chat.domain.vo.response.message;

import com.kkuil.blackchat.domain.dto.IpInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description 返回消息对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResp {

    /**
     * 发送者的信息
     */
    private UserInfo fromUser;

    /**
     * 消息体
     */
    private Message message;

    /**
     * 用户信息（TODO 暂时只先保存用户ID，后需要再进行添加，因为用户如果能发消息，就已经代表已上线，上线列表已经获取到，本地缓存已经生效，本地缓存了用户的姓名和头像，没必要再进行重新获取）
     */
    @Data
    @Builder
    public static class UserInfo {
        /**
         * 用户ID
         */
        private Long uid;

        /**
         * 用户名
         */
        private String name;

        /**
         * 用户头像
         */
        private String avatar;

        /**
         * 用户ip信息
         */
        private IpInfo ipInfo;
    }

    /**
     * 消息体
     */
    @Data
    @Builder
    public static class Message {
        /**
         * 消息ID
         */
        private Long id;

        /**
         * 发送时间
         */
        private Date sendTime;

        /**
         * 消息类型
         */
        private Integer type;

        /**
         * 消息体
         */
        private Object body;

        /**
         * 父消息，如果没有父消息，返回的是null
         */
        private ReplyMsg reply;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReplyMsg {
        /**
         * 消息id
         */
        private Long id;
        /**
         * 用户uid
         */
        private Long uid;
        /**
         * 用户名称
         */
        private String name;
        /**
         * 消息类型 1正常文本 2.撤回消息
         */
        private Integer type;
        /**
         * 消息内容不同的消息类型，见父消息内容体
         */
        private Object body;
        /**
         * 是否可消息跳转 0否 1是
         */
        private Integer canCallback;
        /**
         * 跳转间隔的消息条数
         */
        private Integer gapCount;
    }
}
