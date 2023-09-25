package com.kkuil.blackchat.web.wechat.adapter;

import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

/**
 * @Author Kkuil
 * @Date 2023/9/25 0:13
 * @Description 微信返回文本构造器适配器
 */
public class WechatTextBuilderAdapter {

    /**
     * @param content   需要返回给用户的内容
     * @param wxMessage 微信消息对象
     * @return {@link WxMpXmlOutMessage}
     */
    public WxMpXmlOutMessage build(
            String content,
            WxMpXmlMessage wxMessage
    ) {
        return WxMpXmlOutMessage
                .TEXT()
                .content(content)
                .fromUser(wxMessage.getFromUser())
                .toUser(wxMessage.getToUser())
                .build();
    }
}
