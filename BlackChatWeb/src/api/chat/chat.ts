import request from "@/utils/request";
import { ChatMessage } from "@/layout/components/chat/ChatContent/type";
import { TOKEN_KEY_IN_HEADER, TOKEN_KEY_IN_LOC, TOKEN_PREFIX } from "@/constant/auth";

/**
 * 发送消息
 * @returns
 */
export const sendMessage = (
    data: ChatMessage.ChatMessageBaseReq<any>
): Promise<GlobalTypes.ApiResult<string[]>> => {
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
