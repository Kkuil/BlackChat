import { WorkerTypeEnum } from "@/core/websocket/domain/enum/WorkerTypeEnum"
import { WsRequestTypeEnum } from "@/core/websocket/domain/enum/WsRequestTypeEnum"
import { WsResponseTypeEnum } from "@/core/websocket/domain/enum/WsResponseTypeEnum"

export declare namespace WebsocketTypes {
    /**
     * 工作进程的参数类型
     */
    type WorkerParamsType = {
        type: WorkerTypeEnum
        data?: Record<string, any>
    }

    /**
     * Websocket的参数类型
     */
    type WebsocketReqType = {
        type: WsRequestTypeEnum
        data?: string
    }

    /**
     * Websocket的返回类型
     */
    type WebsocketRespType = {
        type: WsResponseTypeEnum
        data?: string
    }
}
