import { worker } from "./initWorker"
import { WebsocketTypes } from "@/core/websocket/types/type"
import { WorkerTypeEnum } from "@/core/websocket/domain/WorkerTypeEnum"

class websocket {
    constructor() {
        // 初始化连接
        this.initConnect()
    }

    initConnect() {
        const message: WebsocketTypes.WorkerParamsType = {
            type: WorkerTypeEnum.INIT,
            data: "123"
        }
        worker.postMessage(JSON.stringify(message))
    }
}

export default new websocket()
