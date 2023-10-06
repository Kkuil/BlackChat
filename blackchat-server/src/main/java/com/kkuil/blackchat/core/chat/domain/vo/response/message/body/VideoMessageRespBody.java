package com.kkuil.blackchat.core.chat.domain.vo.response.message.body;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Kkuil
 * @Date 2023/10/4 16:50
 * @Description 图片消息响应体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoMessageRespBody {
    /**
     *  大小（字节）
     */
    private Long size;
    /**
     * 下载地址
     */
    private String url;
    /**
     * 视频名
     */
    private String videoName;
}
