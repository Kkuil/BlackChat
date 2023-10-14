import request from "@/utils/request";
import { ChatMessageReq } from "@/layout/components/chat/ChatContent/ChatMessageReq";
import { TOKEN_KEY_IN_HEADER, TOKEN_KEY_IN_LOC, TOKEN_PREFIX } from "@/constant/auth";
import { ContactReq } from "@/layout/components/chat/ChatList/ContactReq";
import type { Session } from "@/stores/session";

/**
 * 获取会话信息
 * @returns
 */
export const getSessionByRoomId = (
    roomId: number
): Promise<GlobalTypes.ApiResult<Session & { totalCount: number }>> => {
    return request({
        url: "/contact",
        method: "GET",
        params: {
            roomId
        },
        headers: {
            [TOKEN_KEY_IN_HEADER]:
                TOKEN_PREFIX + localStorage.getItem(TOKEN_KEY_IN_LOC) || ""
        }
    })
}

/**
 * 用户阅读信息上报
 * @returns
 */
export const pushReadMessage = (
    data: ContactReq.ContactReadMessageReq
): Promise<GlobalTypes.ApiResult<boolean>> => {
    return request({
        url: "/contact/read-message",
        method: "POST",
        data,
        headers: {
            [TOKEN_KEY_IN_HEADER]:
                TOKEN_PREFIX + localStorage.getItem(TOKEN_KEY_IN_LOC) || ""
        }
    })
}
