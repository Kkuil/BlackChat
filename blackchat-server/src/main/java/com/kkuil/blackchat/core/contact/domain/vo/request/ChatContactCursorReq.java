package com.kkuil.blackchat.core.contact.domain.vo.request;

import com.kkuil.blackchat.domain.vo.request.CursorPageBaseReq;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author Kkuil
 * @Date 2023/10/3 12:30
 * @Description 消息列表请求
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class ChatContactCursorReq extends CursorPageBaseReq<String> {

}
