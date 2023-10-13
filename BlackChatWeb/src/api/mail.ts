import request from "@/utils/request";
import { TOKEN_KEY_IN_HEADER, TOKEN_KEY_IN_LOC, TOKEN_PREFIX } from "@/constant/auth";
import type { MailReq } from "@/layout/components/ToolSide/components/type.d.ts";

/**
 * 消息操作
 * @returns
 */
export const mailOperation = (
    params: MailReq
): Promise<GlobalTypes.ApiResult<boolean>> => {
    return request({
        url: "/mail/operation",
        method: "GET",
        params,
        headers: {
            [TOKEN_KEY_IN_HEADER]:
                TOKEN_PREFIX + localStorage.getItem(TOKEN_KEY_IN_LOC) || ""
        }
    })
}
