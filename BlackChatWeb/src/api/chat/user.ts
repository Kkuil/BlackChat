import request from "@/utils/request";
import { MessageBody } from "@/layout/components/ChatContent/type";

/**
 * 发送消息
 * @returns
 */
export const sendMessage = (
    data: MessageBody
): Promise<GlobalTypes.ApiResult<string[]>> => {
    return request({
        url: "/chat/send",
        method: "POST",
        data: data
    })
}
