package com.kkuil.blackchat.web.wechat.handler;

import com.kkuil.blackchat.web.wechat.service.WxMessageService;
import jakarta.annotation.Resource;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author Kkuil
 * @Date 2023/9/25
 * @Description 扫码处理器
 */
@Component
public class ScanHandler extends AbstractHandler {


    @Resource
    private WxMessageService wxMessageService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map,
                                    WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        // 扫码事件处理
        return wxMessageService.scan(wxMpService, wxMpXmlMessage);
    }

}
