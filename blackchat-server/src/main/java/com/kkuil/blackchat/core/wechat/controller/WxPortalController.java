package com.kkuil.blackchat.core.wechat.controller;

import com.kkuil.blackchat.core.wechat.service.WxMessageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;


/**
 * @Author Kkuil
 * @Date 2023/9/24
 * @Description 微信便捷登录接口
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("wx/portal/public")
public class WxPortalController {

    /**
     * 微信AES加密算法
     */
    public static final String AES = "aes";
    public static final String REDIRECT_URL = "https://mp.weixin.qq.com/s/m1SRsBG96kLJW5mPe4AVGA";

    private final WxMpService wxService;
    private final WxMpMessageRouter messageRouter;
    private final WxMessageService wxMessageService;

    /**
     * 微信授权方法
     *
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce     防重放
     * @param echostr   随机字符串，用户回应微信
     * @return {@link String}
     */
    @GetMapping(produces = "text/plain;charset=utf-8")
    public String authGet(
            @RequestParam(name = "signature", required = false) String signature,
            @RequestParam(name = "timestamp", required = false) String timestamp,
            @RequestParam(name = "nonce", required = false) String nonce,
            @RequestParam(name = "echostr", required = false) String echostr
    ) {
        log.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature,
                timestamp, nonce, echostr);
        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }
        if (wxService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }
        return "非法请求";
    }

    /**
     * @param code 随机码
     * @return {@link RedirectView}
     */
    @GetMapping("/callBack")
    public RedirectView callBack(@RequestParam String code) {
        try {
            WxOAuth2AccessToken accessToken = wxService.getOAuth2Service().getAccessToken(code);
            WxOAuth2UserInfo userInfo = wxService.getOAuth2Service().getUserInfo(accessToken, "zh_CN");
            wxMessageService.authorize(userInfo);
        } catch (Exception e) {
            log.error("回调失败", e);
        }
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(REDIRECT_URL);
        return redirectView;
    }

    /**
     * @param requestBody  请求体
     * @param signature    签名
     * @param timestamp    时间戳
     * @param nonce        防重放
     * @param openid       用户openid
     * @param encType      加密方式
     * @param msgSignature 消息签名
     * @return {@link String}
     */
    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String post(
            @RequestBody String requestBody,
            @RequestParam("signature") String signature,
            @RequestParam("timestamp") String timestamp,
            @RequestParam("nonce") String nonce,
            @RequestParam("openid") String openid,
            @RequestParam(name = "encrypt_type", required = false) String encType,
            @RequestParam(name = "msg_signature", required = false) String msgSignature
    ) {
        log.info("\n接收微信请求：[openid=[{}], [signature=[{}], encType=[{}], msgSignature=[{}],"
                        + " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
                openid, signature, encType, msgSignature, timestamp, nonce, requestBody);

        if (!wxService.checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }

        String out = null;
        // 明文传输的消息
        if (encType == null) {
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return "";
            }
            out = outMessage.toXml();
        }
        // aes加密的消息
        if (AES.equalsIgnoreCase(encType)) {
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody, wxService.getWxMpConfigStorage(),
                    timestamp, nonce, msgSignature);
            log.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return "";
            }
            out = outMessage.toEncryptedXml(wxService.getWxMpConfigStorage());
        }

        log.debug("\n组装回复信息：{}", out);
        return out;
    }

    /**
     * 路由信息
     *
     * @param message 消息
     * @return {@link WxMpXmlOutMessage}
     */
    private WxMpXmlOutMessage route(WxMpXmlMessage message) {
        try {
            return this.messageRouter.route(message);
        } catch (Exception e) {
            log.error("路由消息时出现异常！", e);
        }

        return null;
    }
}
