package com.kkuil.blackchat.core.wechat.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.kkuil.blackchat.dao.UserDAO;
import com.kkuil.blackchat.domain.entity.User;
import com.kkuil.blackchat.service.LoginService;
import com.kkuil.blackchat.service.UserService;
import com.kkuil.blackchat.core.websocket.service.WebSocketService;
import com.kkuil.blackchat.core.wechat.adapter.UserAdapter;
import com.kkuil.blackchat.core.wechat.adapter.WechatTextBuilderAdapter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;


import java.net.URLEncoder;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Kkuil
 * @Date 2023/9/24
 * @Description 处理与微信api的交互逻辑
 */
@Service
@Slf4j
public class WxMessageService {

    /**
     * 用户的openId和前端登录场景code的映射关系
     */
    private static final ConcurrentHashMap<String, Integer> OPENID_EVENT_CODE_MAP = new ConcurrentHashMap<>();

    /**
     * 重定向URL
     */
    private static final String URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";

    /**
     * 给用户发送的授权消息
     */
    public static final String SKIP_URL_SCHEMA = "点击链接授权：<a href=\"%s\">登录</a>";

    @Value("${self.wechat.callback}")
    private String callback;

    @Value("${self.spring.server.context-path}")
    private String prefix = "";

    @Resource
    private UserDAO userDao;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private LoginService loginService;
    @Resource
    @Lazy
    private WebSocketService webSocketService;
    @Resource
    private UserService userService;

    /**
     * 扫码 (能触发这个事件的前提都是建立在已经关注公众号的基础上，千万别踩坑)
     *
     * @param wxMpXmlMessage 微信xml消息
     * @return 返回消息
     */
    public WxMpXmlOutMessage scan(WxMpService wxMpService, WxMpXmlMessage wxMpXmlMessage) {
        // 获取扫码用户的openid
        String openId = wxMpXmlMessage.getFromUser();
        Integer code = Integer.parseInt(replacePrefixIfPresent(wxMpXmlMessage));
        // 判断是否已经注册，且授权成功了（名字头像等信息已经获取到了）
        // 判断用户是否注册过
        User user = userDao.getByOpenId(openId);
        // TODO 1. 已注册，也授权了，直接登录
        if (!ObjectUtil.isNull(user) && !StrUtil.isBlank(user.getAvatar())) {
            login(user.getId(), code);
            return new WechatTextBuilderAdapter().build("登录成功，已上线", wxMpXmlMessage);
        }
        // TODO 2. 已注册，但是未授权，让用户授权
        // 授权链接
        String url = String.format(URL, wxMpService.getWxMpConfigStorage().getAppId(), URLEncoder.encode(callback + prefix + "/wx/portal/public/callBack"));
        String format = String.format(SKIP_URL_SCHEMA, url);
        return new WechatTextBuilderAdapter().build(format, wxMpXmlMessage);
    }

    /**
     * 订阅即登录
     *
     * @param wxMpService    微信消息
     * @param wxMpXmlMessage 微信xml消息
     * @return 返回消息
     */
    public WxMpXmlOutMessage subscribe(WxMpService wxMpService, WxMpXmlMessage wxMpXmlMessage) {
        // 获取到当前用户的openid
        String openId = wxMpXmlMessage.getFromUser();
        Integer code = Integer.parseInt(replacePrefixIfPresent(wxMpXmlMessage));
        // 判断用户是否注册过
        User user = userDao.getByOpenId(openId);
        if (!ObjectUtil.isNull(user) && !StrUtil.isBlank(user.getAvatar())) {
            // 登录
            login(user.getId(), code);
        }
        if (ObjectUtil.isNull(user)) {
            // 注册，这时候只保存用户的openid，名字和头像信息都没办法进行保存（因为没权限）
            userService.register(openId);
            // TODO 这个地方是新加的，因为如果用户没有关注过该公众号，扫码不会触发微信的扫码事件，
            //  我们也就无法存储code -> channel的映射关系，后续的扫码成功事件也就没办法通过code拿到channel，
            //  然后推送给前端进行提示
            // 保存openid和场景code的关系，后续才能通知到前端
            OPENID_EVENT_CODE_MAP.put(openId, code);
            // 授权流程,给用户发送授权消息，并且异步通知前端订阅成功
            threadPoolTaskExecutor.execute(() -> {
                // 判断如果等待登录队列中无该码，则表示已经超时或者已经被移除了
                webSocketService.subscribeSuccess(code);
            });
        }
        String url = String.format(
                URL,
                wxMpService
                        .getWxMpConfigStorage()
                        .getAppId(),
                URLEncoder.encode(callback + prefix + "/wx/portal/public/callBack")
        );
        String format = String.format(SKIP_URL_SCHEMA, url);
        return new WechatTextBuilderAdapter().build("感谢关注，" + format, wxMpXmlMessage);
    }

    /**
     * 只有当用户点击授权链接，我们才可以获取到用户的完整信息，然后进行保存
     *
     * @param userInfo 用户信息
     */
    public void authorize(WxOAuth2UserInfo userInfo) {
        User user = userDao.getByOpenId(userInfo.getOpenid());
        // 更新用户信息
        if (StringUtils.isEmpty(user.getName())) {
            fillUserInfo(user.getId(), userInfo);
        }
        // 触发用户登录成功操作
        Integer code = OPENID_EVENT_CODE_MAP.get(userInfo.getOpenid());
        login(user.getId(), code);
    }

    /**
     * 登录
     *
     * @param uid      用户ID
     * @param code code
     */
    private void login(Long uid, Integer code) {
        User user = userDao.getById(uid);
        // 调用用户登录模块
        String token = loginService.login(uid);
        // 推送前端登录成功
        webSocketService.scanLoginSuccess(code, user, token);
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

    /**
     * 填充用户信息
     *
     * @param uid      用户ID
     * @param userInfo 用户信息
     */
    private void fillUserInfo(Long uid, WxOAuth2UserInfo userInfo) {
        User update = UserAdapter.buildAuthorizeUser(uid, userInfo);
        for (int i = 0; i < 5; i++) {
            try {
                userDao.updateById(update);
                return;
            } catch (DuplicateKeyException e) {
                log.info("fill userInfo duplicate uid:{},info:{}", uid, userInfo);
            } catch (Exception e) {
                log.error("fill userInfo fail uid:{},info:{}", uid, userInfo);
            }
            update.setName("名字重置" + RandomUtil.randomInt(100000));
        }
    }
}
