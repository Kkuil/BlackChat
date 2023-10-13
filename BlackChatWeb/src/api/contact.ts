import request from "@/utils/request"
import { ChatMessageReq } from "@/layout/components/chat/ChatContent/ChatMessageReq"
import {
    TOKEN_KEY_IN_HEADER,
    TOKEN_KEY_IN_LOC,
    TOKEN_PREFIX
} from "@/constant/auth"
import { ContactReq } from "@/layout/components/chat/ChatList/ContactReq"

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
