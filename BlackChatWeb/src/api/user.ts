import request from "@/utils/request"
import {
    TOKEN_KEY_IN_HEADER,
    TOKEN_KEY_IN_LOC,
    TOKEN_PREFIX
} from "@/constant/auth"
import ApiResult = GlobalTypes.ApiResult

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
