import { worker } from "./initWorker"
import { WebsocketRespType, WebsocketTypes } from "@/core/websocket/types/type"
import { WorkerTypeEnum } from "@/core/websocket/domain/enum/WorkerTypeEnum"
import { TOKEN_KEY_IN_LOC } from "@/constant/auth"
import { WsResponseTypeEnum } from "@/core/websocket/domain/enum/WsResponseTypeEnum"
import eventBus from "@/utils/eventBus"
import { WsEventEnum } from "@/enums/websocket/WsEventEnum"
import { ElMessage } from "element-plus"
import { WsRequestTypeEnum } from "@/core/websocket/domain/enum/WsRequestTypeEnum"

class Websocket {
    // 是否连接准备
    #connectReady = false
    // 心跳包计时器
    #heartBeatTimer: number = null

    constructor() {
        // 初始化连接
        this.initConnect()
        // 监听消息
        worker.addEventListener("message", this.onWorkerMessage)
        // 监听当前窗口的可见性
        window.addEventListener("visibilitychange", this.#onVisibilityChange)
        // 发送心跳包，用来告诉服务器当前客户端还处于连接状态
        this.#sendHeartBeat()
    }

    /**
     * 初始化连接
     */
    initConnect() {
        const message: WebsocketTypes.WorkerParamsType = {
            type: WorkerTypeEnum.INIT,
            data: localStorage.getItem(TOKEN_KEY_IN_LOC)
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
        this.#heartBeatTimer && clearInterval(this.#heartBeatTimer)
    }

    /**
     * 收到消息回调
     * @param data
     */
    onMessage = (data: string) => {
        const params: WebsocketRespType = JSON.parse(data)
        console.log(params.type)
        switch (params.type) {
            // 连接成功
            case WsResponseTypeEnum.CONN_SUCCESS: {
                break
            }
            // 收到消息
            case WsResponseTypeEnum.MESSAGE: {
                console.log("收到消息", params.data)
                break
            }
            // 获取登录二维码
            case WsResponseTypeEnum.LOGIN_URL: {
                const url = params.data.url
                eventBus.emit(WsEventEnum.GET_QR_CODE, { url })
                break
            }
            // 订阅成功
            case WsResponseTypeEnum.LOGIN_SUBSCRIBE_SUCCESS: {
                eventBus.emit(WsEventEnum.SUBSCRIBE_SUCCESS, {
                    message: params.data
                })
                break
            }
            // 登录成功
            case WsResponseTypeEnum.LOGIN_SUCCESS: {
                eventBus.emit(WsEventEnum.LOGIN_SUCCESS, {
                    message: params.data
                })
                break
            }
            // 登录失效
            case WsResponseTypeEnum.INVALIDATE_TOKEN: {
                ElMessage.error("登录失效，请重新登录")
            }
        }
    }

    /**
     * 监听当前窗口的可见性
     */
    #onVisibilityChange = () => {
        if (document.hidden) {
            this.#heartBeatTimer && clearInterval(this.#heartBeatTimer)
        } else {
            if (!this.#connectReady) {
                // 重新连接
                this.initConnect()
            }
            this.#sendHeartBeat()
        }
    }

    /**
     * 发送心跳包
     */
    #sendHeartBeat() {
        this.#heartBeatTimer && clearInterval(this.#heartBeatTimer)
        const self = this
        this.#heartBeatTimer = setInterval(() => {
            const heartPack = {
                type: WorkerTypeEnum.MESSAGE,
                data: JSON.stringify({
                    type: WsRequestTypeEnum.HEARTBEAT
                })
            }
            self.send(heartPack)
        }, 1000)
    }
}

export default new Websocket()
