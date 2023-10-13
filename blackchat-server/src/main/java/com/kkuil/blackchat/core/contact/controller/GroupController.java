package com.kkuil.blackchat.core.contact.controller;

import com.kkuil.blackchat.core.user.domain.vo.request.CreateGroupReq;
import com.kkuil.blackchat.domain.dto.RequestHolderDTO;
import com.kkuil.blackchat.service.GroupMemberService;
import com.kkuil.blackchat.utils.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Kkuil
 * @Date 2023/10/9 19:31
 * @Description 群控制器
 */
@RequestMapping("group")
@RestController
public class GroupController {

    @Resource
    private GroupMemberService groupMemberService;

    /**
     * 退出群聊
     *
     * @param groupId 群组ID
     * @return 是否退出
     */
    @DeleteMapping("exit")
    @Operation(summary = "退出群组", description = "退出群组")
    public ResultUtil<Boolean> exitGroup(Long groupId) {
        Long uid = RequestHolderDTO.get().getUid();
        return ResultUtil.success(groupMemberService.exitGroup(uid, groupId), true);
    }

    /**
     * 创建群聊
     *
     * @param createGroupReq 请求信息
     * @return 是否创建成功
     */
    @PostMapping("create-group")
    @Operation(summary = "创建群聊", description = "创建群聊")
    public ResultUtil<Boolean> creatGroup(@RequestBody CreateGroupReq createGroupReq) {
        Long uid = RequestHolderDTO.get().getUid();
        return ResultUtil.success(groupMemberService.createGroup(uid, createGroupReq), true);
    }

}
