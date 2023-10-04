import request from "@/utils/request"
import type { UploadRawFile } from "element-plus"
import {
    TOKEN_KEY_IN_HEADER,
    TOKEN_KEY_IN_LOC,
    TOKEN_PREFIX
} from "@/constant/auth"
import ApiResult = GlobalTypes.ApiResult

/**
 * 上传图片
 * @param image
 */
export const upload = (image: UploadRawFile): Promise<ApiResult<string>> => {
    return request({
        url: "/upload/image",
        method: "POST",
        data: {
            image
        },
        headers: {
            "Content-Type": "multipart/form-data",
            [TOKEN_KEY_IN_HEADER]:
                TOKEN_PREFIX + localStorage.getItem(TOKEN_KEY_IN_LOC) || ""
        }
    })
}

/**
 * 上传文件
 * @param file
 */
export const uploadFile = (file: UploadRawFile): Promise<ApiResult<string>> => {
    return request({
        url: "/upload/file",
        method: "POST",
        data: {
            file
        },
        headers: {
            "Content-Type": "multipart/form-data",
            [TOKEN_KEY_IN_HEADER]:
                TOKEN_PREFIX + localStorage.getItem(TOKEN_KEY_IN_LOC) || ""
        }
    })
}

/**
 * 上传视频
 * @param video
 */
export const uploadVideo = (
    video: UploadRawFile
): Promise<ApiResult<string>> => {
    return request({
        url: "/upload/video",
        method: "POST",
        data: {
            video
        },
        headers: {
            "Content-Type": "multipart/form-data",
            [TOKEN_KEY_IN_HEADER]:
                TOKEN_PREFIX + localStorage.getItem(TOKEN_KEY_IN_LOC) || ""
        }
    })
}
