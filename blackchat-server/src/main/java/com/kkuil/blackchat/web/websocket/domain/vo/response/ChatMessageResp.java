package com.kkuil.blackchat.web.websocket.domain.vo.response;

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

    private UserInfo fromUser;

    private Message message;

    @Data
    public static class UserInfo {
        private Long uid;
    }

    @Data
    public static class Message {
        private Long id;
        private Date sendTime;
        private Integer type;
        private Object body;
        private MessageMark messageMark;
    }

    @Data
    public static class MessageMark {
        private Integer likeCount;
        private Integer userLike;
        private Integer dislikeCount;
        private Integer userDislike;
    }
}
