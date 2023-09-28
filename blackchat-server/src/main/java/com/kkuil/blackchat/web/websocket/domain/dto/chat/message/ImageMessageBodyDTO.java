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
 * @Description 图片消息入参
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ImageMessageBodyDTO extends AbstractChatMessageBaseReq implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 大小（字节）
     */
    @NotNull
    private Long size;

    /**
     * 宽度（像素）
     */
    @NotNull
    private Integer width;

    /**
     * 高度（像素）
     */
    @NotNull
    private Integer height;

    /**
     * 下载地址
     */
    @NotBlank
    private String url;
}
