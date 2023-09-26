package com.kkuil.blackchat.web.websocket.adapter;

import com.kkuil.blackchat.domain.entity.User;
import com.kkuil.blackchat.web.websocket.domain.enums.WsResponseTypeEnum;
import com.kkuil.blackchat.web.websocket.domain.vo.WsLoginVO;
import com.kkuil.blackchat.web.websocket.domain.vo.response.WsBaseResp;
import com.kkuil.blackchat.web.websocket.domain.vo.response.WsLoginSuccess;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.stereotype.Component;

/**
 * @Author Kkuil
 * @Date 2023/9/25 17:28
 * @Description websocket适配器
 */
@Component
public class WsAdapter {

    /**
     * 构建登录返回请求
     *
     * @param wxMpQrCodeTicket 微信登录二维码票据
     * @return WsBaseResp<WsLoginVO>
     */
    public static WsBaseResp<WsLoginVO> buildLoginResp(WxMpQrCodeTicket wxMpQrCodeTicket) {
        WsBaseResp<WsLoginVO> wsBaseResp = new WsBaseResp<>();
        wsBaseResp.setType(WsResponseTypeEnum.LOGIN_URL.getType());
        wsBaseResp.setData(new WsLoginVO().setUrl(wxMpQrCodeTicket.getUrl()));
        return wsBaseResp;
    }

    /**
     * 构建扫码成功返回体
     *
     * @return 扫码成功返回体
     */
    public static WsBaseResp<?> buildScanSuccessResp() {
        WsBaseResp<?> wsBaseResp = new WsBaseResp<>();
        wsBaseResp.setType(WsResponseTypeEnum.LOGIN_SCAN_SUCCESS.getType());
        return wsBaseResp;
    }

    /**
     * 构建登录成功
     * @param user  用户信息
     * @param token token
     *
     * @return 消息体
     */
    public static WsBaseResp<?> buildLoginSuccessResp(User user, String token) {
        WsBaseResp<WsLoginSuccess> wsBaseResp = new WsBaseResp<>();
        wsBaseResp.setType(WsResponseTypeEnum.LOGIN_SUCCESS.getType());
        WsLoginSuccess wsLoginSuccess = WsLoginSuccess.builder()
                .avatar(user.getAvatar())
                .name(user.getName())
                .token(token)
                .uid(user.getId())
                .build();
        wsBaseResp.setData(wsLoginSuccess);
        return wsBaseResp;
    }
}
