import { worker } from "./initWorker"
import {
    WebsocketParamsType,
    WebsocketTypes
} from "@/core/websocket/types/type"
import { WorkerTypeEnum } from "@/core/websocket/domain/enum/WorkerTypeEnum"
import { TOKEN_KEY_IN_LOC } from "@/constant/auth"
import { WsResponseTypeEnum } from "@/core/websocket/domain/enum/WsResponseTypeEnum"
import eventBus from "@/utils/eventBus"
import { WsEventEnum } from "@/enums/websocket/WsEventEnum"

class Websocket {
    constructor() {
        // 初始化连接
        this.initConnect()
        // 监听消息
        worker.addEventListener("message", this.onWorkerMessage)
    }

    /**
     * 初始化连接
     */
    initConnect() {
        const message: WebsocketTypes.WorkerParamsType = {
            type: WorkerTypeEnum.INIT,
            data: localStorage.getItem(TOKEN_KEY_IN_LOC) ?? ""
        }
        worker.postMessage(JSON.stringify(message))
    }

    /**
     * 发送消息
     */
    send(message: WebsocketTypes.WorkerParamsType) {
        worker.postMessage(JSON.stringify(message))
    }

    /**
     * 监听主进程消息
     * @param e 消息事件对象
     */
    onWorkerMessage = (e: MessageEvent<string>) => {
        const params: { type: WorkerTypeEnum; value?: unknown } = JSON.parse(
            e.data
        )
        switch (params.type) {
            case WorkerTypeEnum.MESSAGE: {
                this.onMessage(params.value)
                break
            }
            case WorkerTypeEnum.OPEN: {
                // this.#dealTasks()
                break
            }
            case WorkerTypeEnum.CLOSE:
            case WorkerTypeEnum.ERROR: {
                // this.#onClose()
                break
            }
        }
    }

    /**
     * 收到消息回调
     * @param data
     */
    onMessage = (data: string) => {
        const params: WebsocketParamsType = JSON.parse(data)
        switch (params.type) {
            case WsResponseTypeEnum.CONN_SUCCESS: {
                break
            }
            case WsResponseTypeEnum.MESSAGE: {
                console.log("收到消息", params.data)
                break
            }
            case WsResponseTypeEnum.LOGIN_URL: {
                const url = params.data.url
                eventBus.emit(WsEventEnum.GET_QR_CODE, { url })
            }
        }
    }
}

export default new Websocket()
