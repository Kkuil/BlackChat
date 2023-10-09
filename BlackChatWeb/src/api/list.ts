import request from "@/utils/request"
import {
    TOKEN_KEY_IN_HEADER,
    TOKEN_KEY_IN_LOC,
    TOKEN_PREFIX
} from "@/constant/auth"
import ApiResult = GlobalTypes.ApiResult
import UserInfo = GlobalTypes.UserInfo;
import LimitPage = GlobalTypes.LimitPage;
import CursorPageResp = GlobalTypes.CursorPageResp;

/**
 * 获取成员列表
 *
 * @param params CursorPage
 */
export const listMember = (
    params: GlobalTypes.CursorPageReq
): Promise<
    ApiResult<
        CursorPageResp<UserInfo> & {
            extraInfo: { activeStatus: number; totalCount: number }
        }
    >
> => {
    return request({
        url: "/chat/member/list",
        method: "GET",
        params: {
            pageSize: params.pageSize,
            cursor: params.cursor,
            roomId: params.roomId,
            activeStatus: params.activeStatus
        },
        headers: {
            [TOKEN_KEY_IN_HEADER]:
                TOKEN_PREFIX + localStorage.getItem(TOKEN_KEY_IN_LOC) || ""
        }
    })
}

/**
 * 获取消息列表
 *
 * @param params CursorPage
 */
export const listMessage = (
    params: GlobalTypes.CursorPageReq
): Promise<
    ApiResult<CursorPageResp<ChatMessageResp.ChatMessageBaseResp<any, any>>>
> => {
    return request({
        url: "/chat/message/list",
        method: "GET",
        params: {
            pageSize: params.pageSize,
            cursor: params.cursor,
            roomId: params.roomId
        },
        headers: {
            [TOKEN_KEY_IN_HEADER]:
                TOKEN_PREFIX + localStorage.getItem(TOKEN_KEY_IN_LOC) || ""
        }
    })
}

/**
 * 获取上徽章
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

/**
 * 获取会话列表
 *
 * @param params CursorPage
 */
export const listSession = (
    params: GlobalTypes.CursorPageReq
): Promise<ApiResult<CursorPageResp<Contact>>> => {
    return request({
        url: "/contact/list",
        method: "GET",
        params,
        headers: {
            [TOKEN_KEY_IN_HEADER]:
                TOKEN_PREFIX + localStorage.getItem(TOKEN_KEY_IN_LOC) || ""
        }
    })
}

/**
 * 获取朋友列表
 */
export const listFriend = (): Promise<ApiResult<Store.FriendInfo[]>> => {
    return request({
        url: "/friend/list",
        method: "GET",
        headers: {
            [TOKEN_KEY_IN_HEADER]:
                TOKEN_PREFIX + localStorage.getItem(TOKEN_KEY_IN_LOC) || ""
        }
    })
}
