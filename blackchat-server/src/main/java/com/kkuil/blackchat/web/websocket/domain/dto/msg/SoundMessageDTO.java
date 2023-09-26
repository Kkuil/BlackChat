package com.kkuil.blackchat.web.websocket.domain.dto.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description 语音消息入参
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SoundMessageDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull
    private Long size;

    @NotNull
    private Integer second;

    @NotBlank
    private String url;
}
