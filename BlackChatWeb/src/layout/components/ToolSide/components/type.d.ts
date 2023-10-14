import { ReadStatusEnum } from "@/enums/ReadStatusEnum"

export declare namespace ToolSide {
    type ReceiveMessageReq = {
        status: ReadStatusEnum
    }
    type ReceiveMessageResp = {
        /**
         * 消息ID
         */
        id: number
        /**
         * 消息的发送者ID  TODO: 在这里不传用户名和头像是因为前端有缓存则走缓存，减少带宽
         */
        uid: number
        /**
         * 消息的发送者头像
         */
        avatar: string
        /**
         * 消息的发送者名字
         */
        name: string
        /**
         * 备注
         */
        msg: string
        /**
         * 消息内容
         */
        content: string
        /**
         * 消息类型 1未读 2已读
         */
        type: number
    }
    type MailReq = {
        /**
         * 消息ID
         */
        id: number
        /**
         * 消息类型 2同意 3拒绝
         */
        status: number
    }
}
