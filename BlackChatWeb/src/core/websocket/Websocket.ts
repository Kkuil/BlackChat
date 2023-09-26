import { worker } from "./initWorker"
import { WebsocketRespType, WebsocketTypes } from "@/core/websocket/types/type"
import { WorkerTypeEnum } from "@/core/websocket/domain/enum/WorkerTypeEnum"
import { TOKEN_KEY_IN_LOC } from "@/constant/auth"
import { WsResponseTypeEnum } from "@/core/websocket/domain/enum/WsResponseTypeEnum"
import eventBus from "@/utils/eventBus"
import { WsEventEnum } from "@/enums/websocket/WsEventEnum"
import { ElMessage } from "element-plus"

class Websocket {
    #connectReady = false

    constructor() {
        // 初始化连接
        this.initConnect()
        // 监听消息
        worker.addEventListener("message", this.onWorkerMessage)
        // 监听当前窗口的可见性
        window.addEventListener("visibilitychange", this.#onVisibilityChange)
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
        this.#connectReady = true
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
                this.#onClose()
                break
            }
        }
    }

    /**
     * 重置一些属性
     * @private
     */
    #onClose = () => {
        this.#connectReady = false
    }

    /**
     * 收到消息回调
     * @param data
     */
    onMessage = (data: string) => {
        const params: WebsocketRespType = JSON.parse(data)
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
                break
            }
            case WsResponseTypeEnum.LOGIN_SCAN_SUCCESS: {
                ElMessage.success("扫码成功")
                eventBus.emit("scan_success", { message: params.data })
                break
            }
            case WsResponseTypeEnum.LOGIN_SUCCESS: {
                ElMessage.success("登录成功")
                eventBus.emit("login_success", { message: params.data })
                break
            }
        }
    }

    /**
     * 监听当前窗口的可见性
     */
    #onVisibilityChange = () => {
        // 判断当前是否已连接
        if (!document.hidden && !this.#connectReady) {
            // 重新连接
            this.initConnect()
        }
    }
}

export default new Websocket()
