package com.kkuil.blackchat.domain.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * @Author Kkuil
 * @Date 2023/10/1 17:39
 * @Description 用户ip信息
 */
@Data
public class IpInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 注册时的ip
     */
    private String createIp;

    /**
     * 注册时的ip详情
     */
    private IpDetail createIpDetail;

    /**
     * 最新登录的ip
     */
    private String updateIp;

    /**
     * 最新登录的ip详情
     */
    private IpDetail updateIpDetail;

    public void refreshIp(String ip) {
        if (StringUtils.isEmpty(ip)) {
            return;
        }
        updateIp = ip;
        if (createIp == null) {
            createIp = ip;
        }
    }

    /**
     * 需要刷新的ip，这里判断更新ip就够，初始化的时候ip也是相同的，只需要设置的时候多设置进去就行
     *
     * @return
     */
    public String needRefreshIp() {
        boolean notNeedRefresh = Optional.ofNullable(updateIpDetail)
                .map(IpDetail::getIp)
                .filter(ip -> Objects.equals(ip, updateIp))
                .isPresent();
        return notNeedRefresh ? null : updateIp;
    }

    public void refreshIpDetail(IpDetail ipDetail) {
        if (Objects.equals(createIp, ipDetail.getIp())) {
            createIpDetail = ipDetail;
        }
        if (Objects.equals(updateIp, ipDetail.getIp())) {
            updateIpDetail = ipDetail;
        }
    }
}
