import request from "@/utils/request";

/**
 * 获取登录二维码
 * @returns
 */
export const getLoginQrCode = (): Promise<GlobalTypes.ApiResult<string>> => {
    return request({
        url: "/login",
        method: "GET"
    })
}
