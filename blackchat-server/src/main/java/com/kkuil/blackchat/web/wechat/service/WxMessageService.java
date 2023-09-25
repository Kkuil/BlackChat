package com.kkuil.blackchat.web.wechat.service;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author Kkuil
 * @Date 2023/9/24
 * @Description 处理与微信api的交互逻辑
 */
@Service
@Slf4j
public class WxMessageService {
    private static final String URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";

    @Value("${self.wechat.callback}")
    private String callback;

    public WxMpXmlOutMessage scan(WxMpService wxMpService, WxMpXmlMessage wxMpXmlMessage) {
        return null;
    }

    public void authorize(WxOAuth2UserInfo userInfo) {
    }

    private void fillUserInfo(Long uid, WxOAuth2UserInfo userInfo) {
    }
}
