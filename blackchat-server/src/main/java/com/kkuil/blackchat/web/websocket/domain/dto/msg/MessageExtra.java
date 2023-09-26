package com.kkuil.blackchat.web.websocket.domain.dto.msg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kkuil.blackchat.utils.discover.domain.UrlInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description 消息扩展属性
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageExtra implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * url跳转链接
     */
    private Map<String, UrlInfo> urlContentMap;
    /**
     * 消息撤回详情
     */
    private MessageRecallDTO recall;
    /**
     * 艾特的uid
     */
    private List<Long> atUidList;
    /**
     * 文件消息
     */
    private FileMessageDTO fileMsg;
    /**
     * 图片消息
     */
    private ImgMessageDTO imgMessageDTO;
    /**
     * 语音消息
     */
    private SoundMessageDTO soundMessageDTO;
    /**
     * 文件消息
     */
    private VideoMessageDTO videoMessageDTO;

    /**
     * 表情图片信息
     */
    private EmojisMessageDTO emojisMessageDTO;
}
