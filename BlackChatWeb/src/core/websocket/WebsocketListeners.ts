import { pushMessageToMainThread } from "./Worker"
import { WorkerTypeEnum } from "@/core/websocket/domain/enum/WorkerTypeEnum"

export default {
    onError: () => {
        pushMessageToMainThread({ type: WorkerTypeEnum.ERROR })
    },
    onClose: () => {
        pushMessageToMainThread({ type: WorkerTypeEnum.CLOSE })
    },
    onOpen: () => {
        pushMessageToMainThread({ type: WorkerTypeEnum.OPEN })
    },
    onMessage: (e: MessageEvent<string>) => {
        pushMessageToMainThread({ type: WorkerTypeEnum.MESSAGE, value: e.data })
    }
}
