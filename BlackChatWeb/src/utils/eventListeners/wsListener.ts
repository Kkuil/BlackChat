import eventBus from "@/utils/eventBus";
import Websocket from "@/core/websocket/Websocket";
import { WsEventEnum } from "@/enums/websocket/WsEventEnum";
import { LOGIN_MESSAGE } from "@/core/websocket/domain/preset/WsMessagePreset";

/**
 * 监听刷新二维码事件
 */
eventBus.on(WsEventEnum.REFRESH_QR_CODE, () => {
    Websocket.send(LOGIN_MESSAGE)
})
