import { pushMessageToMainThread } from "./Worker"
import { WorkerTypeEnum } from "@/core/websocket/domain/WorkerTypeEnum"

export default {
    onError: () => {
        pushMessageToMainThread({ type: WorkerTypeEnum.ERROR })
    },
    onClose: () => {
        pushMessageToMainThread({ type: WorkerTypeEnum.CLOSE })
    },
    onOpen: () => {
        console.log(13)
        pushMessageToMainThread({ type: WorkerTypeEnum.OPEN })
    },
    onMessage: (e: never) => {
        console.log(e.value)
        pushMessageToMainThread({ type: WorkerTypeEnum.MESSAGE, value: e.data })
    }
}
