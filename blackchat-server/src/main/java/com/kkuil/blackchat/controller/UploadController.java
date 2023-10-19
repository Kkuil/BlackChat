package com.kkuil.blackchat.controller;

import com.kkuil.blackchat.anotations.FrequencyControl;
import com.kkuil.blackchat.domain.dto.RequestHolderDTO;
import com.kkuil.blackchat.service.impl.AliyunOssService;
import com.kkuil.blackchat.utils.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

import static com.kkuil.blackchat.constant.UploadConst.*;

/**
 * @Author Kkuil
 * @Date 2023/10/4 11:00
 * @Description 上传控制器
 */
@RestController
@RequestMapping("upload")
public class UploadController {

    @Resource
    private AliyunOssService aliyunOssService;

    /**
     * 上传图片
     *
     * @param image 对象
     * @return 访问地址
     */
    @PostMapping("image")
    @Operation(summary = "上传图片", description = "上传图片")
    @Parameters(
            value = {
                    @Parameter(name = "image", description = "图片", required = true, example = "image"),
            }
    )
    @FrequencyControl(time = 5, count = 3, target = FrequencyControl.Target.UID)
    public ResultUtil<String> uploadImage(@RequestBody MultipartFile image) {
        Long uid = RequestHolderDTO.get().getUid();
        String filename = uid + "_" + System.currentTimeMillis() + "." + Objects.requireNonNull(image.getOriginalFilename()).split("\\.")[1];
        String path = DEFAULT_IMAGE_PATH + filename;
        String url = aliyunOssService.upload(path, image, DEFAULT_IMAGE_MAX_SIZE);
        return ResultUtil.success(url);
    }

    /**
     * 上传文件
     *
     * @param file 对象
     * @return 访问地址
     */
    @PostMapping("file")
    @Operation(summary = "上传文件", description = "上传文件")
    @Parameters(
            value = {
                    @Parameter(name = "file", description = "图片", required = true, example = "file"),
            }
    )
    @FrequencyControl(time = 5, count = 3, target = FrequencyControl.Target.UID)
    public ResultUtil<String> uploadFile(@RequestBody MultipartFile file) {
        Long uid = RequestHolderDTO.get().getUid();
        String filename = uid + "_" + System.currentTimeMillis() + "." + Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[1];
        String path = DEFAULT_FILE_PATH + filename;
        String url = aliyunOssService.upload(path, file, DEFAULT_FILE_MAX_SIZE);
        return ResultUtil.success(url);
    }

    /**
     * 上传视频
     *
     * @param video 对象
     * @return 访问地址
     */
    @PostMapping("video")
    @Operation(summary = "上传视频", description = "上传视频")
    @Parameters(
            value = {
                    @Parameter(name = "video", description = "视频", required = true, example = "video"),
            }
    )
    @FrequencyControl(time = 5, count = 3, target = FrequencyControl.Target.UID)
    public ResultUtil<String> uploadVideo(@RequestBody MultipartFile video) {
        Long uid = RequestHolderDTO.get().getUid();
        String filename = uid + "_" + System.currentTimeMillis() + "." + Objects.requireNonNull(video.getOriginalFilename()).split("\\.")[1];
        String path = DEFAULT_VIDEO_PATH + filename;
        String url = aliyunOssService.upload(path, video, DEFAULT_VIDEO_MAX_SIZE);
        return ResultUtil.success(url);
    }

    /**
     * 上传群头像
     *
     * @param groupAvatar 对象
     * @return 访问地址
     */
    @PostMapping("group-avatar")
    @Operation(summary = "上传群头像", description = "上传群头像")
    @Parameters(
            value = {
                    @Parameter(name = "groupAvatar", description = "群头像", required = true, example = "groupAvatar"),
            }
    )
    @FrequencyControl(time = 5, count = 3, target = FrequencyControl.Target.UID)
    public ResultUtil<String> uploadGroupAvatar(@RequestBody MultipartFile groupAvatar) {
        String path = DEFAULT_GROUP_AVATAR_PATH + groupAvatar.getOriginalFilename();
        String url = aliyunOssService.upload(path, groupAvatar, DEFAULT_GROUP_AVATAR_MAX_SIZE);
        return ResultUtil.success(url);
    }
}
