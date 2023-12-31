package com.kkuil.blackchat.core.websocket.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kkuil.blackchat.constant.RedisKeyConst;
import com.kkuil.blackchat.core.websocket.domain.enums.WsResponseTypeEnum;
import com.kkuil.blackchat.dao.UserDAO;
import com.kkuil.blackchat.dao.UserRoleDAO;
import com.kkuil.blackchat.domain.entity.User;
import com.kkuil.blackchat.event.UserOfflineEvent;
import com.kkuil.blackchat.event.UserOnlineEvent;
import com.kkuil.blackchat.event.adapter.EventParamAdapter;
import com.kkuil.blackchat.event.domain.dto.UserOnlineEventParamsDTO;
import com.kkuil.blackchat.service.LoginService;
import com.kkuil.blackchat.utils.RedisUtil;
import com.kkuil.blackchat.core.websocket.adapter.WsAdapter;
import com.kkuil.blackchat.core.websocket.constant.AuthorizationConst;
import com.kkuil.blackchat.core.websocket.domain.dto.WsConnInfoDTO;
import com.kkuil.blackchat.core.websocket.domain.vo.response.WsBaseResp;
import com.kkuil.blackchat.core.websocket.domain.vo.response.WsLoginSuccessMessage;
import com.kkuil.blackchat.core.websocket.service.WebSocketService;
import com.kkuil.blackchat.core.websocket.utils.NettyUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import static com.kkuil.blackchat.constant.RedisKeyConst.LOGIN_CODE;
import static com.kkuil.blackchat.constant.UserConst.TEMP_USER_UID;
import static com.kkuil.blackchat.core.websocket.domain.enums.WsResponseTypeEnum.CONN_SUCCESS;

/**
 * @Author Kkuil
 * @Date 202309/17 17:00:00
 * @Description websocket服务类
 */
@Service
@Slf4j
public class WebSocketServiceImpl implements WebSocketService {

    /**
     * 所有已连接的websocket连接和相关信息的映射
     */
    public static final ConcurrentHashMap<Channel, WsConnInfoDTO> CHANNEL_CONN_MAP = new ConcurrentHashMap<>();

    /**
     * 所有已连接的在线用户ID和连接通道的映射
     * <p>
     * 这里使用数组的原因是为了适应单账户多端登录的场景也可以同步消息
     */
    public static final ConcurrentHashMap<Long, CopyOnWriteArrayList<Channel>> UID_CHANNEL_MAP = new ConcurrentHashMap<>();

    /**
     * 所有请求登录的code与channel关系
     */
    public static final Cache<Integer, Channel> WAIT_LOGIN_MAP = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofHours(1))
            .maximumSize(10000L)
            .build();

    /**
     * 登录有效期
     */
    private static final Duration LOGIN_EXPIRE_TIME = Duration.ofHours(1);

    /**
     * 设置每个账户的最大登录数
     */
    public static final Integer MAX_CONCURRENT_LONGIN = 3;

    @Resource
    private WxMpService wxMpService;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;
    @Resource
    private UserRoleDAO userRoleDao;
    @Resource
    private LoginService loginService;
    @Resource
    private UserDAO userDao;

    /**
     * 用户连接
     *
     * @param channel 用户通道
     */
    @Override
    public void connect(Channel channel) {
        CHANNEL_CONN_MAP.put(channel, new WsConnInfoDTO());
        WsBaseResp<String> wsBaseResp = new WsBaseResp<>();
        String token = loginService.login(TEMP_USER_UID);
        wsBaseResp.setType(CONN_SUCCESS.getType()).setData(token);
        sendMsgToOne(channel, wsBaseResp);
        log.info("CHANNEL_CONN_MAP: {}", CHANNEL_CONN_MAP);
    }

    /**
     * 断开连接
     *
     * @param channel 连接通道
     */
    @Override
    public void disconnect(Channel channel) {
        // 删除全部连接（包括未登录的）
        CHANNEL_CONN_MAP.remove(channel);
        // 删除用户连接（设置为离线）
        // 判断当前连接是否是以登录态
        Long uid = NettyUtil.getAttrFromChannel(channel, AuthorizationConst.UID_KEY_IN_CHANNEL);
        if (uid == null) {
            return;
        }
        offline(channel, uid);
    }

    /**
     * 用户上线
     *
     * @param channel 连接通道
     * @param uid     用户ID
     */
    @Override
    public void online(Channel channel, Long uid) {
        // 记录当前连接信息
        UID_CHANNEL_MAP.putIfAbsent(uid, new CopyOnWriteArrayList<>());
        // 这里必须使用add，不然不支持单账户多端登录
        // 这里可以限制登录设备台数
        CopyOnWriteArrayList<Channel> channels = UID_CHANNEL_MAP.get(uid);
        if (TEMP_USER_UID.equals(uid) || channels.size() < MAX_CONCURRENT_LONGIN) {
            channels.add(channel);
        } else {
            WsBaseResp<Void> wsBaseResp = new WsBaseResp<>();
            wsBaseResp.setType(WsResponseTypeEnum.LIMIT_CONCURRENT_LOGIN.getType());
            sendMsgToOne(channel, wsBaseResp);
        }
        // 将用户id记录在当前通道中
        NettyUtil.setAttrInChannel(channel, AuthorizationConst.UID_KEY_IN_CHANNEL, uid);
    }

    /**
     * 下线
     *
     * @param channel 断开连接
     */
    @Override
    public void offline(Channel channel, Long uid) {
        // 删除本地缓存UID列表
        UID_CHANNEL_MAP.remove(uid);
        // 发布下线事件
        applicationEventPublisher.publishEvent(new UserOfflineEvent(this, uid));
    }

    /**
     * 扫码登录
     *
     * @param channel 连接通道
     */
    @Override
    @SneakyThrows
    public void scan(Channel channel) {
        // 生成随机不重复的登录码,并将channel存在本地cache中
        Integer code = generateLoginCode(channel);
        // 获取登录二维码
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(code, (int) LOGIN_EXPIRE_TIME.getSeconds());
        // 发送登录二维码给前端
        sendMsgToOne(channel, WsAdapter.buildLoginResp(wxMpQrCodeTicket));
    }

    /**
     * 当用户扫码成功时，返回前端提示用户扫码成功
     *
     * @param code 登录码
     */
    @Override
    public void subscribeSuccess(Integer code) {
        if (code == null) {
            // TODO 这个地方是防止意外情况的发生：
            //  例如：当用户不是从扫码登录过来的，而是直接通过微信搜索进行了订阅公众号，从而导致报错
            return;
        }
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(code);
        if (Objects.isNull(channel)) {
            // 超时或已移除 code -> channel ×
            // TODO 通知用户二维码已过期，刷新二维码
            return;
        }
        Long uid = NettyUtil.getAttrFromChannel(channel, AuthorizationConst.UID_KEY_IN_CHANNEL);
        String ip = NettyUtil.getAttrFromChannel(channel, AuthorizationConst.IP_KEY_IN_CHANNEL);
        User user = userDao.getById(uid);
        User update = new User();
        update.setId(user.getId());
        user.refreshIp(ip);
        update.setIpInfo(user.getIpInfo());
        update.setLastOptTime(new Date());
        sendMsgToOne(channel, WsAdapter.buildSubscribeSuccessResp());
    }

    /**
     * 扫码登录成功
     *
     * @param code  登录码
     * @param user  用户信息
     * @param token token
     */
    @Override
    public void scanLoginSuccess(Integer code, User user, String token) {
        // 发送消息
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(code);
        // 判断等待列表中是否存在该通道
        if (Objects.isNull(channel)) {
            return;
        }
        // 移除code
        WAIT_LOGIN_MAP.invalidate(code);
        // 用户登录
        loginSuccess(channel, user, token);
    }

    /**
     * 授权
     *
     * @param channel 当前通道
     * @param token   用户token
     */
    @Override
    public void authorize(Channel channel, String token) {
        // 校验token
        boolean verifySuccess = loginService.verify(token);
        if (verifySuccess) {
            // 用户校验成功给用户登录
            User user = userDao.getById(loginService.getValidUid(token));
            loginSuccess(channel, user, token);
        } else {
            // 让前端的token失效
            sendMsgToOne(channel, WsAdapter.buildInvalidateTokenResp());
        }
    }

    /**
     * 向某个人发送消息
     *
     * @param channel 通道
     */
    @Override
    public void sendMsgToOne(Channel channel, WsBaseResp<?> wsBaseResp) {
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(wsBaseResp)));
    }

    /**
     * 向某个人发送消息
     *
     * @param uid        用户ID
     * @param wsBaseResp 消息体
     */
    @Override
    public void sendMsgToOne(Long uid, WsBaseResp<?> wsBaseResp) {
        CopyOnWriteArrayList<Channel> channels = UID_CHANNEL_MAP.get(uid);
        if (CollectionUtil.isEmpty(channels)) {
            log.info("用户：{}不在线", uid);
            return;
        }
        channels.forEach(channel -> {
            threadPoolTaskExecutor.execute(() -> sendMsgToOne(channel, wsBaseResp));
        });
    }

    /**
     * 向全部人推送消息（不跳过任何一个人）
     */
    @Override
    public void sendMsgToAll(WsBaseResp<?> wsBaseResp) {
        sendMsgToAll(wsBaseResp, null);
    }

    /**
     * 向全部人推送消息
     *
     * @param wsBaseResp 发送消息体
     * @param skipUid    跳过的用户ID
     */
    @Override
    public void sendMsgToAll(WsBaseResp<?> wsBaseResp, Long skipUid) {
        CHANNEL_CONN_MAP.forEach((channel, ext) -> {
            if (Objects.nonNull(skipUid) && Objects.equals(ext.getUid(), skipUid)) {
                return;
            }
            threadPoolTaskExecutor.execute(() -> sendMsgToOne(channel, wsBaseResp));
        });
    }

    /**
     * 退出登录
     *
     * @param channel 通道
     */
    @Override
    public void logout(Channel channel) {
        Long uid = NettyUtil.getAttrFromChannel(channel, AuthorizationConst.UID_KEY_IN_CHANNEL);
        // 用户下线
        offline(channel, uid);
    }


    /**
     * 保存等待登录码
     * 获取不重复的登录的code，微信要求最大不超过int的存储极限
     * 防止并发，可以给方法加上synchronize，也可以使用cas乐观锁
     *
     * @return 随机码
     */
    public Integer generateLoginCode(Channel channel) {
        int code;
        do {
            // 本地cache时间必须比redis key过期时间短，否则会出现并发问题
            code = RedisUtil.integerInc(RedisKeyConst.getKey(LOGIN_CODE), (int) LOGIN_EXPIRE_TIME.toMinutes(), TimeUnit.MINUTES);
        } while (WAIT_LOGIN_MAP.asMap().containsKey(code));
        // 储存一份在本地
        WAIT_LOGIN_MAP.put(code, channel);
        return code;
    }

    /**
     * 登录成功，并更新状态
     */
    private void loginSuccess(Channel channel, User user, String token) {
        online(channel, user.getId());
        // 构建参数
        // 查询权限
        Long power = userRoleDao.getPower(user);
        WsBaseResp<WsLoginSuccessMessage> wsBaseResp = WsAdapter.buildLoginSuccessResp(user, token, power);
        UserOnlineEventParamsDTO userOnlineEventParamsDTO = EventParamAdapter.buildUserOnlineEventParams(channel, wsBaseResp);
        // 发送用户上线事件
        applicationEventPublisher.publishEvent(new UserOnlineEvent(this, userOnlineEventParamsDTO));
    }
}
