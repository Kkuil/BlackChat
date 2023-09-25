package com.kkuil.blackchat.web.wechat.handler;

import cn.hutool.json.JSONUtil;
import com.kkuil.blackchat.web.wechat.adapter.WechatTextBuilderAdapter;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author Kkuil
 * @Date 2023/9/25
 * @Description 消息处理器
 */
@Component
public class MessageHandler extends AbstractHandler {

    @Override
    public WxMpXmlOutMessage handle(
            WxMpXmlMessage wxMessage,
            Map<String, Object> context,
            WxMpService wxMpService,
            WxSessionManager sessionManager
    ) {
        // 当用户输入关键词如“你好”，“客服”等，并且有客服在线时，把消息转发给在线客服
        try {
            // 判断是否包含客服关键字
            boolean isContainClientKeyword = StringUtils.startsWithAny(wxMessage.getContent(), "你好", "客服");
            // 判断当前是否有客服在线
            boolean isExistOnlineClient = wxMpService.getKefuService().kfOnlineList().getKfOnlineList().size() > 0;
            if (isContainClientKeyword && isExistOnlineClient) {
                return WxMpXmlOutMessage.TRANSFER_CUSTOMER_SERVICE()
                        .fromUser(wxMessage.getToUser())
                        .toUser(wxMessage.getFromUser()).build();
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        // 组装回复消息
        String content = "收到信息内容：" + JSONUtil.toJsonStr(wxMessage);

        return new WechatTextBuilderAdapter().build(content, wxMessage);

    }

}
