import request from "@/utils/request";

/**
 * 获取徽章列表
 * @returns
 */
export const listBadges = (): Promise<GlobalTypes.ApiResult<string[]>> => {
    return request({
        url: "/badge/list",
        method: "GET"
    })
}
