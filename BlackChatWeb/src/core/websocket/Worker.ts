import { WebsocketTypes } from "@/core/websocket/types/type";
import WebsocketListeners from "@/core/websocket/WebsocketListeners.ts";
import { WorkerTypeEnum } from "@/core/websocket/domain/enum/WorkerTypeEnum";
import { WS_URL } from "@/core/websocket/constant/websocketConst";

// 发消息给主进程
export const pushMessageToMainThread = ({
    type,
    value
}: WebsocketTypes.WorkerParamsType) => {
    self.postMessage({ type, value })
}

// ws instance
let connection: WebSocket
// token
let token: string

// 初始化 ws 连接
const initConnection = () => {
    // 移除之前的监听器
    connection?.removeEventListener("message", WebsocketListeners.onMessage)
    connection?.removeEventListener("open", WebsocketListeners.onOpen)
    connection?.removeEventListener("close", WebsocketListeners.onClose)
    connection?.removeEventListener("error", WebsocketListeners.onError)
    // 建立链接
    connection = new WebSocket(`${WS_URL}${token ? `?token=${token}` : ""}`)
    // 收到消息
    connection.addEventListener("message", WebsocketListeners.onMessage)
    // 建立链接
    connection.addEventListener("open", WebsocketListeners.onOpen)
    // 关闭连接
    connection.addEventListener("close", WebsocketListeners.onClose)
    // 连接错误
    connection.addEventListener("error", WebsocketListeners.onError)
}

// 往 ws 发消息
const pushMessageToWs = (data: object) => {
    connection?.send(JSON.stringify(data))
}

// 线程监听消息
self.onmessage = (e: MessageEvent<string>) => {
    const { type, data } = e.data
    switch (type) {
        case WorkerTypeEnum.INIT: {
            console.log("init")
            token = data
            initConnection()
            break
        }
        case WorkerTypeEnum.MESSAGE: {
            // 判断是否已经连接
            if (connection?.readyState !== 1) return
            pushMessageToWs(data)
            break
        }
        default: {
            // TODO
            break
        }
    }
}
