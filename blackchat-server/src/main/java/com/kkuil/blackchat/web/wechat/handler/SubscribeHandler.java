package com.kkuil.blackchat.web.wechat.handler;

import com.kkuil.blackchat.web.wechat.adapter.WechatTextBuilderAdapter;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author Kkuil
 * @Date 2023/9/25
 * @Description 订阅处理器
 */
@Component
public class SubscribeHandler extends AbstractHandler {

    @Override
    public WxMpXmlOutMessage handle(
            WxMpXmlMessage wxMessage,
            Map<String, Object> context,
            WxMpService wxMpService,
            WxSessionManager sessionManager
    ) {

        this.logger.info("新关注用户 OPENID: " + wxMessage.getFromUser());

        try {
            return new WechatTextBuilderAdapter().build("感谢关注", wxMessage);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }

        return null;
    }

}
