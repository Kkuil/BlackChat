import request from "@/utils/request"
import {
    TOKEN_KEY_IN_HEADER,
    TOKEN_KEY_IN_LOC,
    TOKEN_PREFIX
} from "@/constant/auth"
import ApiResult = GlobalTypes.ApiResult

/**
 * 撤回消息
 * @param id 消息ID
 */
export const revoke = (id: number): Promise<ApiResult<boolean>> => {
    return request({
        url: "/message/revoke",
        method: "PUT",
        data: {
            id
        },
        headers: {
            [TOKEN_KEY_IN_HEADER]:
                TOKEN_PREFIX + localStorage.getItem(TOKEN_KEY_IN_LOC) || ""
        }
    })
}
