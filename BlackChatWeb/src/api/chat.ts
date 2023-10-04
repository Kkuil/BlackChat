import request from "@/utils/request";
import { ChatMessageReq } from "@/layout/components/chat/ChatContent/ChatMessageReq";
import { TOKEN_KEY_IN_HEADER, TOKEN_KEY_IN_LOC, TOKEN_PREFIX } from "@/constant/auth";

/**
 * 发送消息
 * @returns
 */
export const sendMessage = (
    data: ChatMessage.ChatMessageBaseReq<any>
): Promise<GlobalTypes.ApiResult<ChatMessageResp.ChatMessageBaseResp>> => {
    return request({
        url: "/chat/send",
        method: "POST",
        data,
        headers: {
            [TOKEN_KEY_IN_HEADER]:
                TOKEN_PREFIX + localStorage.getItem(TOKEN_KEY_IN_LOC) || ""
        }
    })
}
