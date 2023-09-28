// 发送消息类型枚举
export declare namespace ChatMessage {
    /**
     * 发送消息枚举
     */
    enum MessageType {
        TEXT = 1,
        IMAGE = 3,
        FILE = 4,
        AUDIO = 5,
        VIDEO = 6
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
         * 回复的消息id,如果没有别传就好
         */
        replyMessageId: number
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
