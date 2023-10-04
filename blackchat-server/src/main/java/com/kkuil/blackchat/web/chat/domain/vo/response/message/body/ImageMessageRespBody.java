package com.kkuil.blackchat.web.chat.domain.vo.response.message.body;

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
public class ImageMessageRespBody {
    /**
     * 大小（字节）
     */
    private Long size;

    /**
     *  宽度（像素）
     */
    private Integer width;

    /**
     * 高度（像素）
     */
    private Integer height;

    /**
     * 下载地址
     */
    private String url;
}
