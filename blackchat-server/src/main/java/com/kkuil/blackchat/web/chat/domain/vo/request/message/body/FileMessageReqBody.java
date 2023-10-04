package com.kkuil.blackchat.web.chat.domain.vo.request.message.body;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

/**
 * @Author Kkuil
 * @Date 2023/9/28
 * @Description 文件消息入参
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileMessageReqBody implements Serializable {
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
     * 文件名（带后缀）
     */
    @NotBlank
    private String fileName;

}
