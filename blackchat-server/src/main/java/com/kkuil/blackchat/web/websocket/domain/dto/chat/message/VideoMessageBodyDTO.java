package com.kkuil.blackchat.web.websocket.domain.dto.chat.message;

import com.kkuil.blackchat.web.websocket.domain.dto.chat.AbstractChatMessageBaseReq;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

/**
 * @Author Kkuil
 * @Date 2023/9/28
 * @Description 视频消息入参
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VideoMessageBodyDTO extends AbstractChatMessageBaseReq implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 大小（字节）
     */
    @NotNull
    private Long size;

    /**
     * 下载地址
     */
    @NotBlank
    private String url;

    /**
     * 缩略图宽度（像素）
     */
    @NotNull
    private Integer thumbWidth;

    /**
     * 缩略图高度（像素）
     */
    @NotNull
    private Integer thumbHeight;

    /**
     * 缩略图大小（字节）
     */
    @NotNull
    private Long thumbSize;

    /**
     * 缩略图下载地址
     */
    @NotBlank
    private String thumbUrl;

}
