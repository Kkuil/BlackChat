package com.kkuil.blackchat.web.websocket.service.impl;

import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kkuil.blackchat.constant.RedisKeyConst;
import com.kkuil.blackchat.utils.RedisUtil;
import com.kkuil.blackchat.web.websocket.adapter.WsAdapter;
import com.kkuil.blackchat.web.websocket.constant.AuthorizationConst;
import com.kkuil.blackchat.web.websocket.domain.dto.WsConnInfoDTO;
import com.kkuil.blackchat.web.websocket.domain.vo.response.WsBaseResp;
import com.kkuil.blackchat.web.websocket.service.IWebSocketService;
import com.kkuil.blackchat.web.websocket.utils.NettyUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import static com.kkuil.blackchat.constant.RedisKeyConst.LOGIN_CODE;
import static com.kkuil.blackchat.web.websocket.domain.enums.WsResponseTypeEnum.CONN_SUCCESS;

/**
 * @Author Kkuil
 * @Date 202309/17 17:00:00
 * @Description websocket服务类
 */
@Service
@Slf4j
public class WebSocketServiceImpl implements IWebSocketService {

    /**
     * 所有已连接的websocket连接和相关信息的映射
     */
    private static final ConcurrentHashMap<Channel, WsConnInfoDTO> CHANNEL_CONN_MAP = new ConcurrentHashMap<>();

    /**
     * 所有已连接的在线用户ID和连接通道的映射
     */
    private static final ConcurrentHashMap<Long, CopyOnWriteArrayList<Channel>> UID_CHANNEL_MAP = new ConcurrentHashMap<>();


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

    @Resource
    private WxMpService wxMpService;

    /**
     * 用户连接
     *
     * @param channel 用户通道
     */
    @Override
    public void connect(Channel channel) {
        CHANNEL_CONN_MAP.put(channel, new WsConnInfoDTO());
        WsBaseResp<String> wsBaseResp = new WsBaseResp<>();
        wsBaseResp.setType(CONN_SUCCESS.getType()).setData("连接成功");
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
        CHANNEL_CONN_MAP.remove(channel);
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
        UID_CHANNEL_MAP.get(uid).add(channel);
        NettyUtil.setAttrInChannel(channel, AuthorizationConst.UID_KEY_IN_CHANNEL, uid);
    }

    /**
     * 下线
     *
     * @param channel 断开连接
     */
    @Override
    public void offline(Channel channel) {

    }

    /**
     * 处理登录请求
     *
     * @param channel 连接通道
     */
    @Override
    @SneakyThrows
    public void login(Channel channel) {
        // 生成随机不重复的登录码,并将channel存在本地cache中
        Integer code = generateLoginCode(channel);
        // 获取登录二维码
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(code, (int) LOGIN_EXPIRE_TIME.getSeconds());
        // 发送登录二维码给前端
        sendMsgToOne(channel, WsAdapter.buildLoginResp(wxMpQrCodeTicket));
    }

    /**
     * 扫码登录
     *
     * @param code 登录码
     * @param uid  用户ID
     */
    @Override
    public void handleScanLogin(Integer code, Long uid) {

    }

    /**
     * 等待授权事件
     *
     * @param code 登录码
     */
    @Override
    public void waitAuthorize(Integer code) {

    }

    /**
     * 授权
     *
     * @param channel 当前通道
     * @param token   用户token
     */
    @Override
    public void authorize(Channel channel, String token) {

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
     * 向全部人推送消息
     *
     * @param msg 需要推送的消息
     */
    @Override
    public void sendMsgToAll(WsBaseResp<?> msg) {

    }

    /**
     * 获取不重复的登录的code，微信要求最大不超过int的存储极限
     * 防止并发，可以给方法加上synchronize，也可以使用cas乐观锁
     *
     * @return 随机码
     */
    private Integer generateLoginCode(Channel channel) {
        int inc;
        do {
            //本地cache时间必须比redis key过期时间短，否则会出现并发问题
            inc = RedisUtil.integerInc(RedisKeyConst.getKey(LOGIN_CODE), (int) LOGIN_EXPIRE_TIME.toMinutes(), TimeUnit.MINUTES);
        } while (WAIT_LOGIN_MAP.asMap().containsKey(inc));
        //储存一份在本地
        WAIT_LOGIN_MAP.put(inc, channel);
        return inc;
    }
}
