import request from "@/utils/request"
import {
    TOKEN_KEY_IN_HEADER,
    TOKEN_KEY_IN_LOC,
    TOKEN_PREFIX
} from "@/constant/auth"
import ApiResult = GlobalTypes.ApiResult
import UserBaseInfo = GlobalTypes.UserBaseInfo;

/**
 * 更改用户名
 * @param username 用户名
 */
export const updateUsername = (
    username: string
): Promise<ApiResult<boolean>> => {
    return request({
        url: "/user/username",
        method: "PUT",
        params: {
            username
        },
        headers: {
            [TOKEN_KEY_IN_HEADER]:
                TOKEN_PREFIX + localStorage.getItem(TOKEN_KEY_IN_LOC) || ""
        }
    })
}

/**
 * 批量获取用户缓存信息
 * @param uidList 用户ID列表
 */
export const listBatchCache = (
    uidList: number[]
): Promise<ApiResult<UserBaseInfo[]>> => {
    console.log("uidList", uidList)
    return request({
        url: "/public/user/batch-cache",
        method: "GET",
        params: {
            uidList: uidList.join(",")
        }
    })
}

/**
 * 删除好友
 * @param friendId 好友ID
 */
export const delFriend = (friendId: number): Promise<ApiResult<boolean>> => {
    return request({
        url: "/friend/del",
        method: "DELETE",
        params: {
            friendId
        },
        headers: {
            [TOKEN_KEY_IN_HEADER]:
                TOKEN_PREFIX + localStorage.getItem(TOKEN_KEY_IN_LOC) || ""
        }
    })
}
