package com.kkuil.blackchat.core.chat.domain.vo.request.message.body;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

/**
 * @Author Kkuil
 * @Date 2023/9/28
 * @Description 表情图片消息入参
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmojisMessageReqBody implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * url
     */
    @NotBlank
    private String url;
}
