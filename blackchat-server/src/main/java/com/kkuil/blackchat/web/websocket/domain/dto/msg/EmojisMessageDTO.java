package com.kkuil.blackchat.web.websocket.domain.dto.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description 表情图片消息入参
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmojisMessageDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String url;
}
