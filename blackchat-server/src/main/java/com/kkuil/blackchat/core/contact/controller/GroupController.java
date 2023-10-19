package com.kkuil.blackchat.core.contact.controller;

import com.kkuil.blackchat.core.contact.domain.vo.request.AddAdminReq;
import com.kkuil.blackchat.core.contact.domain.vo.request.DelAdminReq;
import com.kkuil.blackchat.core.contact.domain.vo.request.InvitAddGroupReq;
import com.kkuil.blackchat.core.contact.domain.vo.request.UpdateGroupInfoReq;
import com.kkuil.blackchat.core.user.domain.vo.request.CreateGroupReq;
import com.kkuil.blackchat.domain.dto.RequestHolderDTO;
import com.kkuil.blackchat.service.GroupMemberService;
import com.kkuil.blackchat.service.RoomGroupService;
import com.kkuil.blackchat.utils.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @Resource
    private RoomGroupService roomGroupService;

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
    @PostMapping("create")
    @Operation(summary = "创建群聊", description = "创建群聊")
    public ResultUtil<Boolean> creatGroup(@Valid @RequestBody CreateGroupReq createGroupReq) {
        Long uid = RequestHolderDTO.get().getUid();
        return ResultUtil.success(groupMemberService.createGroup(uid, createGroupReq), true);
    }

    /**
     * 邀请加群
     *
     * @param inviteAddGroupReq 邀请加群的请求信息
     * @return 是否邀请成功
     */
    @PostMapping("invite")
    @Operation(summary = "邀请加群", description = "邀请加群")
    public ResultUtil<Boolean> inviteGroup(@Valid @RequestBody InvitAddGroupReq inviteAddGroupReq) {
        Long uid = RequestHolderDTO.get().getUid();
        return ResultUtil.success(groupMemberService.inviteGroup(uid, inviteAddGroupReq));
    }

    /**
     * 添加管理
     *
     * @param addAdminReq 添加管理请求信息
     * @return 是否添加成功
     */
    @PutMapping("admin/add")
    @Operation(summary = "添加管理", description = "添加管理")
    public ResultUtil<Boolean> addAdmin(@Valid @RequestBody AddAdminReq addAdminReq) {
        Long uid = RequestHolderDTO.get().getUid();
        return ResultUtil.success(groupMemberService.addAdmin(uid, addAdminReq));
    }

    /**
     * 删除管理
     *
     * @param delAdminReq 删除管理请求信息
     * @return 是否删除成功
     */
    @PutMapping("admin/update")
    @Operation(summary = "删除管理", description = "删除管理")
    public ResultUtil<Boolean> delAdmin(@Valid @RequestBody DelAdminReq delAdminReq) {
        Long uid = RequestHolderDTO.get().getUid();
        return ResultUtil.success(groupMemberService.delAdmin(uid, delAdminReq));
    }

    /**
     * 更改群信息
     *
     * @param updateGroupInfoReq 更改群信息请求信息
     * @return 是否更新成功
     */
    @PutMapping("info/update")
    @Operation(summary = "更改群信息", description = "更改群信息")
    public ResultUtil<Boolean> updateGroupInfo(@Valid @RequestBody UpdateGroupInfoReq updateGroupInfoReq) {
        Long uid = RequestHolderDTO.get().getUid();
        return ResultUtil.success(roomGroupService.updateGroupInfo(uid, updateGroupInfoReq));
    }
}
