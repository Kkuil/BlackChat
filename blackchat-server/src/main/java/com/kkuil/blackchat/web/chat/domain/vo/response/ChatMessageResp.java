package com.kkuil.blackchat.web.chat.domain.vo.response;

import com.kkuil.blackchat.domain.entity.MessageMark;
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
         * 该条消息的点赞和点踩信息
         */
//        private MessageMark messageMark;
    }

    /**
     * 该条消息的点赞和点踩信息
     */
//    @Data
//    public static class MessageMark {
//        /**
//         * 喜欢数
//         */
//        private Integer likeCount;
//
//        /**
//         * 该名用户是否点赞
//         */
//        private Integer userLike;
//
//        /**
//         * 点踩数
//         */
//        private Integer dislikeCount;
//
//        /**
//         * 该名用户是否点踩
//         */
//        private Integer userDislike;
//    }
}
