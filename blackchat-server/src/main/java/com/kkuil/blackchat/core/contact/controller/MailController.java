package com.kkuil.blackchat.core.contact.controller;

import com.kkuil.blackchat.core.contact.domain.vo.request.MessageOperationReq;
import com.kkuil.blackchat.core.contact.domain.vo.request.MessageReq;
import com.kkuil.blackchat.core.contact.domain.vo.response.MessageResp;
import com.kkuil.blackchat.domain.common.page.PageReq;
import com.kkuil.blackchat.domain.common.page.PageRes;
import com.kkuil.blackchat.domain.dto.RequestHolderDTO;
import com.kkuil.blackchat.service.UserApplyService;
import com.kkuil.blackchat.utils.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author Kkuil
 * @Date 2023/10/12 22:46
 * @Description 申请控制器
 */
@RestController
@RequestMapping("mail")
public class MailController {

    @Resource
    private UserApplyService userApplyService;

    /**
     * 获取消息列表
     *
     * @param pageReq 消息请求体
     * @return 消息列表
     */
    @GetMapping("list")
    @Operation(summary = "获取用户消息", description = "获取用户消息")
    public ResultUtil<PageRes<MessageResp>> listMessage(PageReq<String> pageReq) {
        Long uid = RequestHolderDTO.get().getUid();
        return ResultUtil.success(userApplyService.listMessage(uid, pageReq));
    }

    /**
     * 消息操作
     *
     * @param messageOperationReq 消息操作请求体
     * @return 是否操作成功
     */
    @GetMapping("operation")
    @Operation(summary = "消息操作", description = "消息操作")
    public ResultUtil<Boolean> operation(@Valid MessageOperationReq messageOperationReq) {
        Long uid = RequestHolderDTO.get().getUid();
        return ResultUtil.success(userApplyService.operation(uid, messageOperationReq));
    }

}
