import { worker } from "./initWorker"
import { WebsocketTypes } from "@/core/websocket/types/type"
import { WorkerTypeEnum } from "@/core/websocket/domain/WorkerTypeEnum"
import { TOKEN_KEY_IN_LOC } from "@/constant/auth"

class websocket {
    constructor() {
        // 初始化连接
        this.initConnect()
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
}

export default new websocket()
