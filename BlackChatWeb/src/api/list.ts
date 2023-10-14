import request from "@/utils/request"
import {
    TOKEN_KEY_IN_HEADER,
    TOKEN_KEY_IN_LOC,
    TOKEN_PREFIX
} from "@/constant/auth"
import { ToolSide } from "@/layout/components/ToolSide/components/type.d.ts"
import { ChatMessageResp } from "@/layout/components/chat/ChatContent/ChatMessageResp"
import { ReadStatusEnum } from "@/enums/ReadStatusEnum"
import ApiResult = GlobalTypes.ApiResult
import UserInfo = GlobalTypes.UserInfo;
import LimitPage = GlobalTypes.LimitPage;
import CursorPageResp = GlobalTypes.CursorPageResp;
import PageResp = GlobalTypes.PageResp;

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
        url: "/message/list",
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

/**
 * 获取收件箱列表
 */
export const listReceiveMessage = (
    params: LimitPage<ReadStatusEnum>
): Promise<ApiResult<PageResp<ToolSide.ReceiveMessageResp>>> => {
    return request({
        url: "/mail/list",
        method: "GET",
        params,
        headers: {
            [TOKEN_KEY_IN_HEADER]:
                TOKEN_PREFIX + localStorage.getItem(TOKEN_KEY_IN_LOC) || ""
        }
    })
}
