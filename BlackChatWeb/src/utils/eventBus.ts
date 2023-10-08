import type { Emitter } from "mitt"
import mitt from "mitt"
import { MessageResponseTypes } from "@/core/websocket/types/MessageResponseTypes"
import { ChatMessageResp } from "@/layout/components/chat/ChatContent/ChatMessageResp"

type Events = {
    // 刷新二维码事件
    refresh_qr_code: void
    // 获取二维码事件
    get_qr_code: { url: string }
    // 登录成功事件
    login_success: { message: MessageResponseTypes.TLoginSuccess }
    // 扫码成功事件
    subscribe_success: void
    // 发送消息事件
    send_message: { message: ChatMessageResp.ChatMessageBaseResp<any, any> }
    // 艾特事件
    aite: { people: { uid: number; name: string } }
}

const eventHub: Emitter<Events> = mitt<Events>()
export default eventHub
