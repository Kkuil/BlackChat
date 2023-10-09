package com.kkuil.blackchat.service.impl;

import com.kkuil.blackchat.dao.ContactDAO;
import com.kkuil.blackchat.dao.GroupMemberDAO;
import com.kkuil.blackchat.dao.MessageDAO;
import com.kkuil.blackchat.dao.RoomDAO;
import com.kkuil.blackchat.domain.entity.Room;
import com.kkuil.blackchat.domain.enums.ChatMessageEnum;
import com.kkuil.blackchat.domain.enums.error.ChatErrorEnum;
import com.kkuil.blackchat.domain.enums.error.CommonErrorEnum;
import com.kkuil.blackchat.service.GroupMemberService;
import com.kkuil.blackchat.utils.AssertUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author Kkuil
 * @Date 2023/10/9 19:38
 * @Description 针对表【group_member(群成员表)】的数据库操作Service实现
 */
@Service
public class GroupMemberServiceImpl implements GroupMemberService {

    @Resource
    private GroupMemberDAO groupMemberDao;

    @Resource
    private ContactDAO contactDao;

    @Resource
    private MessageDAO messageDao;

    @Resource
    private RoomDAO roomDao;

    /**
     * 退出群组
     *
     * @param uid     用户ID
     * @param groupId 群组ID
     * @return 是否退出
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String exitGroup(Long uid, Long groupId) {
        Boolean isGroupShip = groupMemberDao.isGroupShip(groupId, uid);
        AssertUtil.isTrue(isGroupShip, ChatErrorEnum.NOT_IN_GROUP.getMsg());

        Room room = roomDao.getById(groupId);

        if (room.isHotRoom()) {
            return ChatErrorEnum.NOT_ALLOWED_TO_EXIT_ALL_GROUP.getErrorMsg();
        }

        // 删除成员
        Boolean isExitGroup = groupMemberDao.exitGroup(groupId, uid);
        AssertUtil.isTrue(isExitGroup, CommonErrorEnum.SYSTEM_ERROR.getMsg());

        // 删除会话记录
        Boolean isDelContact = contactDao.delContact(groupId, uid);
        AssertUtil.isTrue(isDelContact, CommonErrorEnum.SYSTEM_ERROR.getMsg());

        // 删除消息记录
        Boolean isDelMsg = messageDao.delRecordBatch(groupId, uid);
        AssertUtil.isTrue(isDelMsg, CommonErrorEnum.SYSTEM_ERROR.getMsg());

        return ChatMessageEnum.EXIT_GROUP_SUCCESS.getMsg();
    }

}
