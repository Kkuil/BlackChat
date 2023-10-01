declare namespace ChatMessageResp {
    type MessageBody =
        | TextMessageBody
        | ImageMessageBody
        | FileMessageBody
        | SoundMessageBody
        | VideoMessageBody

    /**
     * 消息发送的基础请求体
     */
    type ChatMessageBaseResp<T extends MessageBody> = {
        fromUser: UserInfo
        message: Message
    }
    type UserInfo = {
        uid: string
    }
    type Message = {
        id: number
        sendTime: string
        type: number
        body: MessageBody
        messageMark: MessageMark
    }
    type MessageMark = {
        likeCount: number
        userLike: number
        dislikeCount: number
        userDislike: number
    }

    /**
     * 文本结构体
     */
    type TextMessageBody = {
        content: string
        urlContentMap: Record<string, UrlInfo>
        atUidList: number[]
        reply: ReplyMsg
    }
    type UrlInfo = {
        title: string
        description: string
        image: string
    }
    type ReplyMsg = {
        id: number
        uid: number
        username: string
        type: number
        body: object
        canCallback: number
        gapCount: number
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
         * 缩略图宽度（像素）
         */
        thumbWidth: number
        /**
         * 缩略图高度（像素）
         */
        thumbHeight: number
        /**
         * 缩略图大小（字节）
         */
        thumbSize: number
        /**
         * 缩略图下载地址
         */
        thumbUrl: string
    }
}
