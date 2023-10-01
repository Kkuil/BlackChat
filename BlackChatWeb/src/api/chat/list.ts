import request from "@/utils/request"
import {
    TOKEN_KEY_IN_HEADER,
    TOKEN_KEY_IN_LOC,
    TOKEN_PREFIX
} from "@/constant/auth"
import CursorPage = GlobalTypes.CursorPage
import ApiResult = GlobalTypes.ApiResult;
import UserInfo = GlobalTypes.UserInfo;
import LimitPage = GlobalTypes.LimitPage;

/**
 * 获取成员列表
 *
 * @param params CursorPage
 */
export const listMember = (
    params: CursorPage
): Promise<ApiResult<UserInfo>> => {
    return request({
        url: "/member/list",
        method: "GET",
        params: {
            pageSize: params.pageSize,
            cursor: params.cursor,
            roomId: params.data
        },
        headers: {
            [TOKEN_KEY_IN_HEADER]:
                TOKEN_PREFIX + localStorage.getItem(TOKEN_KEY_IN_LOC) || ""
        }
    })
}

/**
 * 获取上线列表
 *
 * @param params CursorPage
 */
export const listBadge = (
    params: LimitPage<any>
): Promise<ApiResult<UserInfoDialogCompTypes.Badge>> => {
    return request({
        url: "/user/badge",
        method: "GET",
        params,
        headers: {
            [TOKEN_KEY_IN_HEADER]:
                TOKEN_PREFIX + localStorage.getItem(TOKEN_KEY_IN_LOC) || ""
        }
    })
}
