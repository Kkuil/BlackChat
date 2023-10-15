package com.kkuil.blackchat.core.chat.domain.vo.response.message.body;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kkuil.blackchat.utils.discover.domain.UrlInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Author Kkuil
 * @Date 2023/9/29 11:18
 * @Description 文本消息返回体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TextMessageRespBody {
    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息链接映射
     */
    private Map<String, UrlInfo> urlContentMap;

    /**
     * 艾特的uid
     */
    private List<Long> atUidList;
}
