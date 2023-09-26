import type { WebsocketTypes } from "@/core/websocket/types/type"
import { WsRequestTypeEnum } from "@/core/websocket/domain/enum/WsRequestTypeEnum"
import WebsocketParamsType = WebsocketTypes.WebsocketReqType

export const LOGIN_MESSAGE: WebsocketParamsType = {
    type: WsRequestTypeEnum.LOGIN,
    data: ""
}
