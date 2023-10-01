package com.kkuil.blackchat.web.chat.domain.dto.message;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author Kkuil
 * @Date 2023/9/28
 * @Description 消息撤回
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecallMessageBodyDTO  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 撤回消息的uid
     */
    private Long recallUid;

    /**
     * 撤回的时间点
     */
    private Date recallTime;
}
