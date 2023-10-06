package com.kkuil.blackchat.core.mq.service;

import com.kkuil.blackchat.core.mq.MqProducer;
import com.kkuil.blackchat.core.mq.constant.MqConstant;
import com.kkuil.blackchat.core.mq.domain.dto.PushMessageDTO;
import com.kkuil.blackchat.core.websocket.domain.vo.response.WsBaseResp;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/10/6 13:08
 * @Description 推送服务类
 */
@Service
public class PushService {
    @Resource
    private MqProducer mqProducer;

    public void sendPushMsg(WsBaseResp<?> msg, List<Long> uidList) {
        uidList.parallelStream().forEach(uid -> {
            mqProducer.sendMessage(MqConstant.PUSH_TOPIC, new PushMessageDTO(uid, msg));
        });
    }

    public void sendPushMsg(WsBaseResp<?> msg, Long uid) {
        mqProducer.sendMessage(MqConstant.PUSH_TOPIC, new PushMessageDTO(uid, msg));
    }

    public void sendPushMsg(WsBaseResp<?> msg) {
        mqProducer.sendMessage(MqConstant.PUSH_TOPIC, new PushMessageDTO(msg));
    }
}
