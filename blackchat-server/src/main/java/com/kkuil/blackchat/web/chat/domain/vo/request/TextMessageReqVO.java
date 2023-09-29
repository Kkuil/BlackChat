package com.kkuil.blackchat.web.chat.domain.vo.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/9/28 16:51
 * @Description 文本消息请求体
 */
@Data
@Builder
public class TextMessageReqVO {

    /**
     * 消息内容
     */
    @NotBlank(message = "内容不能为空")
    @Size(max = 1024, message = "消息内容过长，服务器扛不住啊，兄dei")
    private String content;

    /**
     * 艾特的uid
     */
    @Size(max = 10, message = "一次别艾特这么多人")
    private List<Long> atUidList;
}
