package com.kkuil.blackchat.web.chat.domain.dto.message;

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
public class EmojisMessageBodyDTO  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * url
     */
    @NotBlank
    private String url;
}
