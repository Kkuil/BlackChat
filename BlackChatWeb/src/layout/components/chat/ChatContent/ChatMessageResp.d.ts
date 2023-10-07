import type { MessageTypeEnum } from "@/enums/MessageTypeEnum"

export declare namespace ChatMessageResp {
    type MessageBody =
        | TextMessageBody
        | ImageMessageBody
        | FileMessageBody
        | SoundMessageBody
        | VideoMessageBody
        | SystemMessageBody

    /**
     * 消息发送的基础请求体
     */
    type ChatMessageBaseResp<T extends MessageBody, R> = {
        fromUser: UserInfo
        message: Message<T, R>
    }
    type UserInfo = {
        uid: string
        name: string
        avatar: string
        ipInfo?: IpInfo
    }
    type IpInfo = {
        /**
         * 注册时的ip
         */
        createIp: string

        /**
         * 注册时的ip详情
         */
        createIpDetail: IpDetail

        /**
         * 最新登录的ip
         */
        updateIp: string

        /**
         * 最新登录的ip详情
         */
        updateIpDetail: IpDetail
    }
    type IpDetail = {
        ip: string
        isp: string
        isp_id: string
        city: string
        city_id: string
        country: string
        country_id: string
        region: string
        region_id: string
    }
    type Message<T, R> = {
        id: number
        sendTime: string
        type: MessageTypeEnum
        body: T
        reply?: ReplyMsg<R>
    }
    type ReplyMsg<R extends MessageBody> = {
        id: number
        uid: number
        name: string
        type: number
        body: R
        canCallback: number
        gapCount: number
    }

    /**
     * 文本结构体
     */
    type TextMessageBody = {
        content: string
        urlContentMap: Record<string, UrlInfo>
        atUidList: number[]
    }
    type UrlInfo = {
        title: string
        description: string
        image: string
    }

    /**
     * 系统消息体
     */
    type SystemMessageBody = {
        content: string
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
