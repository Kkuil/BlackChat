import request from "@/utils/request"
import {
    TOKEN_KEY_IN_HEADER,
    TOKEN_KEY_IN_LOC,
    TOKEN_PREFIX
} from "@/constant/auth"
import ApiResult = GlobalTypes.ApiResult

/**
 * 退出群聊
 * @param groupId 房间ID
 */
export const exitGroup = (groupId: number): Promise<ApiResult<boolean>> => {
    return request({
        url: "/group/exit",
        method: "DELETE",
        params: {
            groupId
        },
        headers: {
            [TOKEN_KEY_IN_HEADER]:
                TOKEN_PREFIX + localStorage.getItem(TOKEN_KEY_IN_LOC) || ""
        }
    })
}
