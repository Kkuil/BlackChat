import { WebsocketTypes } from "@/core/websocket/types/type";
import Listeners from "./listeners";
import { WorkerTypeEnum } from "@/core/websocket/domain/WorkerTypeEnum";
import { WS_URL } from "@/core/websocket/constant/websocketConst";

// 发消息给主进程
export const pushMessageToMainThread = ({
    type,
    value
}: WebsocketTypes.WorkerParamsType) => {
    self.postMessage(JSON.stringify({ type, value }))
}

// ws instance
let connection: WebSocket
// 初始化 ws 连接
const initConnection = () => {
    // 移除之前的监听器
    connection?.removeEventListener("message", Listeners.onMessage)
    connection?.removeEventListener("open", Listeners.onOpen)
    connection?.removeEventListener("close", Listeners.onClose)
    connection?.removeEventListener("error", Listeners.onError)
    // 建立链接
    // 本地配置到 .env 里面修改。
    console.log(WS_URL)
    connection = new WebSocket(WS_URL)
    // 收到消息
    connection.addEventListener("message", Listeners.onMessage)
    // 建立链接
    connection.addEventListener("open", Listeners.onOpen)
    // 关闭连接
    connection.addEventListener("close", Listeners.onClose)
    // 连接错误
    connection.addEventListener("error", Listeners.onError)
}

// 往 ws 发消息
const pushMessageToWs = (value: object) => {
    connection?.send(JSON.stringify(value))
}

// 主线程监听消息
self.onmessage = (e: MessageEvent<string>) => {
    const { type, value } = JSON.parse(e.data)
    switch (type) {
        case WorkerTypeEnum.INIT: {
            console.log("init")
            initConnection()
            break
        }
        case WorkerTypeEnum.MESSAGE: {
            // 判断是否已经连接
            if (connection?.readyState !== 1) return
            pushMessageToWs(value)
            break
        }
        default: {
            // TODO
            break
        }
    }
}
