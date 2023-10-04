declare namespace ChatMessageReq {
    type MessageBody =
        | TextMessageBody
        | ImageMessageBody
        | FileMessageBody
        | SoundMessageBody
        | VideoMessageBody

    /**
     * 消息发送的基础请求体
     */
    type ChatMessageBaseReq<T extends MessageBody> = {
        /**
         * 房间号
         */
        roomId: number
        /**
         * 消息类型
         */
        messageType: ChatMessageReq.MessageType
        /**
         * 回复的消息id,如果没有别传就好
         */
        replyMessageId: number
        /**
         * 消息体
         */
        body: T
    }

    /**
     * 文本结构体
     */
    type TextMessageBody = {
        /**
         * 文本消息内容
         */
        content: string
        /**
         * 艾特的uid集合
         */
        atUidList: number[]
    }

    /**
     * 图片结构体
     */
    type ImageMessageBody = {
        /**
         * 大小（字节）
         */
        size: number
        /**
         *  宽度（像素）
         */
        width: number
        /**
         * 高度（像素）
         */
        height: number
        /**
         * 下载地址
         */
        url: string
    }

    /**
     * 文件结构体
     */
    type FileMessageBody = {
        /**
         *  大小（字节）
         */
        size: number
        /**
         * 文件名
         */
        fileName: string
        /**
         * 下载地址
         */
        url: string
    }

    /**
     * 语音结构体
     */
    type SoundMessageBody = {
        /**
         *  大小（字节）
         */
        size: number
        /**
         * 时长
         */
        second: number
        /**
         * 下载地址
         */
        url: string
    }

    /**
     * 视频结构体
     */
    type VideoMessageBody = {
        /**
         *  大小（字节）
         */
        size: number
        /**
         * 下载地址
         */
        url: string
        /**
         * 视频名
         */
        videoName: string
    }
}
