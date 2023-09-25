import { WorkerTypeEnum } from "@/core/websocket/domain/WorkerTypeEnum"

export declare namespace WebsocketTypes {
    type WorkerParamsType = {
        type: WorkerTypeEnum
        data?: string
    }
}
