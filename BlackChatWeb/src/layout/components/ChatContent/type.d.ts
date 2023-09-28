import {
    ChatMessage,
    FileMessageBody,
    ImageMessageBody,
    SoundMessageBody,
    TextMessageBody,
    VideoMessageBody
} from "@/layout/components/ChatContent/ChatMessageTypes"

export declare namespace ChatTypes {
    type MessageBody =
        | TextMessageBody
        | ImageMessageBody
        | FileMessageBody
        | SoundMessageBody
        | VideoMessageBody

    /**
     * 消息发送的基础请求体
     */
    type ChatMessageBaseReq = {
        // 房间号
        roomId: number
        // 消息类型
        messageType: ChatMessage.MessageType
        // 消息体
        body: MessageBody
    }
}
