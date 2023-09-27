package com.kkuil.blackchat.web.websocket.adapter;

import com.kkuil.blackchat.domain.entity.User;
import com.kkuil.blackchat.web.websocket.domain.enums.WsResponseTypeEnum;
import com.kkuil.blackchat.web.websocket.domain.vo.WsLoginVO;
import com.kkuil.blackchat.web.websocket.domain.vo.response.WsBaseResp;
import com.kkuil.blackchat.web.websocket.domain.vo.response.WsLoginSuccessMessage;
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
     * 构建订阅成功返回体
     *
     * @return 订阅成功返回体
     */
    public static WsBaseResp<?> buildSubscribeSuccessResp() {
        WsBaseResp<?> wsBaseResp = new WsBaseResp<>();
        wsBaseResp.setType(WsResponseTypeEnum.LOGIN_SUBSCRIBE_SUCCESS.getType());
        return wsBaseResp;
    }

    /**
     * 构建登录成功
     *
     * @param user  用户信息
     * @param token token
     * @param power 用户权限
     * @return 消息体
     */
    public static WsBaseResp<WsLoginSuccessMessage> buildLoginSuccessResp(User user, String token, Long power) {
        WsBaseResp<WsLoginSuccessMessage> wsBaseResp = new WsBaseResp<>();
        wsBaseResp.setType(WsResponseTypeEnum.LOGIN_SUCCESS.getType());
        WsLoginSuccessMessage wsLoginSuccessMessage = WsLoginSuccessMessage.builder()
                .avatar(user.getAvatar())
                .name(user.getName())
                .power(power)
                .token(token)
                .uid(user.getId())
                .build();
        wsBaseResp.setData(wsLoginSuccessMessage);
        return wsBaseResp;
    }

    /**
     * 构建无效token
     *
     * @return WsBaseResp<WsLoginSuccessMessage> 消息体
     */
    public static WsBaseResp<WsLoginSuccessMessage> buildInvalidateTokenResp() {
        WsBaseResp<WsLoginSuccessMessage> wsBaseResp = new WsBaseResp<>();
        wsBaseResp.setType(WsResponseTypeEnum.INVALIDATE_TOKEN.getType());
        return wsBaseResp;
    }
}
