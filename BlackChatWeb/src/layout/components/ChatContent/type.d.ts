export declare namespace ChatTypes {
    // 消息发送的基础请求体
    type ChatMessageBaseReq = {
        // 房间号
        roomId: number
        // 消息类型
        messageType: number
        // 消息体
        body: object
    }

    // 消息类型
    type MessageType = "text" | "image" | "video" | "audio" | "file" | "link"
}
