package com.kkuil.blackchat.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.kkuil.blackchat.dao.UserDAO;
import com.kkuil.blackchat.domain.bo.ip.IpResult;
import com.kkuil.blackchat.domain.dto.IpDetail;
import com.kkuil.blackchat.domain.dto.IpInfo;
import com.kkuil.blackchat.domain.entity.User;
import com.kkuil.blackchat.exception.handler.GlobalUncaughtExceptionHandler;
import com.kkuil.blackchat.service.IpService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author Kkuil
 * @Date 2023/10/4 23:33
 * @Description ip服务类
 */
@Service
@Slf4j
public class IpServiceImpl implements IpService, DisposableBean {

    /**
     * 获取ip信息重试次数
     */
    public static final int RETRY_COUNT = 3;

    /**
     * 获取IP的url
     */
    public static final String URL_GET_IP_DETAIL = "https://ip.taobao.com/outGetIpInfo?ip=%s&accessKey=alibaba-inc";

    /**
     * 优雅停机的最大等待时间
     */
    public static final int SHUTDOWN_MAX_TIMEOUT = 30;
    @Resource
    private UserDAO userDao;

    private static final ExecutorService EXECUTOR = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(500),
            new NamedThreadFactory("refresh-ipDetail", null, false,
                    new GlobalUncaughtExceptionHandler()
            )
    );

    @Override
    public void refreshIpDetailAsync(Long uid) {
        // 异步获取用户信息
        EXECUTOR.execute(() -> {
            User user = userDao.getById(uid);
            IpInfo ipInfo = user.getIpInfo();
            if (Objects.isNull(ipInfo)) {
                return;
            }
            String ip = ipInfo.needRefreshIp();
            if (StrUtil.isBlank(ip)) {
                return;
            }
            IpDetail ipDetail = tryGetIpDetailOrNullTreeTimes(ip);
            if (Objects.nonNull(ipDetail)) {
                ipInfo.refreshIpDetail(ipDetail);
                User update = new User();
                update.setId(uid);
                update.setIpInfo(ipInfo);
                userDao.updateById(update);
            } else {
                log.error("get ip detail fail ip:{},uid:{}", ip, uid);
            }

        });
    }

    /**
     * 尝试异步获取 ip
     *
     * @param ip ip
     * @return ip详情
     */
    private static IpDetail tryGetIpDetailOrNullTreeTimes(String ip) {
        for (int i = 0; i < RETRY_COUNT; i++) {
            IpDetail ipDetail = getIpDetailOrNull(ip);
            if (Objects.nonNull(ipDetail)) {
                return ipDetail;
            }
            try {
                // 减少频繁请求
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static IpDetail getIpDetailOrNull(String ip) {
        String url = String.format(URL_GET_IP_DETAIL, ip);
        String body = HttpUtil.get(url);
        try {
            IpResult<IpDetail> result = JSONUtil.toBean(body, new TypeReference<>() {
            }, false);
            if (result.isSuccess()) {
                return result.getData();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public void destroy() throws InterruptedException {
        EXECUTOR.shutdown();
        if (!EXECUTOR.awaitTermination(SHUTDOWN_MAX_TIMEOUT, TimeUnit.SECONDS)) {
            // 最多等30秒，处理不完直接轻质关停
            if (log.isErrorEnabled()) {
                log.error("Timed out while waiting for executor [{}] to terminate", EXECUTOR);
            }
        }
    }

}
