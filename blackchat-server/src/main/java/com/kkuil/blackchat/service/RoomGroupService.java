package com.kkuil.blackchat.service;

import com.kkuil.blackchat.core.contact.domain.vo.request.UpdateGroupInfoReq;

/**
 * @Author Kkuil
 * @Date 2023/10/18 15:55
 * @Description 针对表【room_group(群聊房间表)】的数据库操作Service
 */
public interface RoomGroupService {

    /**
     * 更改群名
     *
     * @param uid                用户ID
     * @param updateGroupInfoReq 请求信息
     * @return 是否更改群名成功
     */
    Boolean updateGroupInfo(Long uid, UpdateGroupInfoReq updateGroupInfoReq);
}
