package com.kkuil.blackchat.web.wechat.service;

import com.kkuil.blackchat.domain.entity.User;
import com.kkuil.blackchat.web.wechat.adapter.WechatTextBuilderAdapter;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * @Author Kkuil
 * @Date 2023/9/24
 * @Description 处理与微信api的交互逻辑
 */
@Service
@Slf4j
public class WxMessageService {
    /**
     * 重定向URL
     */
    private static final String URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";

    @Value("${self.wechat.callback}")
    private String callback;

    @Value("${self.spring.server.context-path}")
    private String prefix = "";

    public WxMpXmlOutMessage scan(WxMpService wxMpService, WxMpXmlMessage wxMpXmlMessage) {
        // 获取扫码用户的openid
        String openid = wxMpXmlMessage.getFromUser();
        Integer loginCode = Integer.parseInt(replacePrefixIfPresent(wxMpXmlMessage));
        log.info("openid: {}", openid);
        log.info("loginCode: {}", loginCode);
        // TODO 保存用户信息
        // ......
        String skipUrl = String.format(
                URL,
                wxMpService.getWxMpConfigStorage().getAppId(),
                URLEncoder.encode(
                        callback + prefix + "/wx/portal/public/callBack",
                        Charset.defaultCharset()
                )
        );
        return new WechatTextBuilderAdapter().build("点击链接授权：<a href=\"" + skipUrl + "\">登录</a>", wxMpXmlMessage);
    }

    public void authorize(WxOAuth2UserInfo userInfo) {
    }

    private void fillUserInfo(Long uid, WxOAuth2UserInfo userInfo) {
    }

    /**
     * 去除事件前缀
     *
     * @param wxMpXmlMessage 微信对象
     * @return 事件Key
     */
    private String replacePrefixIfPresent(WxMpXmlMessage wxMpXmlMessage) {
        // 扫码关注的渠道事件有前缀，需要去除
        return wxMpXmlMessage.getEventKey().replace("qrscene_", "");
    }
}
