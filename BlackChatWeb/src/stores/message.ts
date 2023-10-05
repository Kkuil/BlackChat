import { ref, watch } from "vue"
import { defineStore } from "pinia"
import { listMessage } from "@/api/list"
import { useSessionStore } from "@/stores/session"
import { MessageTypeEnum } from "@/enums/MessageTypeEnum"

const sessionStore = useSessionStore()

export type TReplyMessage = {
    id: number
    name: string
    content: string
}

export const useMessageStore = defineStore("message", () => {
    const messageInfo = ref<ChatMessageReq.ChatMessageBaseReq<any>>({
        roomId: sessionStore.sessionInfo.chattingId,
        messageType: MessageTypeEnum.TEXT,
        replyMessageId: null,
        body: {
            content: "",
            atUidList: [],
            urlContentMap: {}
        }
    })

    const replyMessage = ref<TReplyMessage>({
        id: null,
        name: null,
        content: null
    })

    const messageList = ref<ChatMessageResp.ChatMessageBaseResp<any, any>[]>([])

    const listPage = ref<
        GlobalTypes.CursorPageReq & {
            isLast: boolean
        }
    >({
        pageSize: 15,
        cursor: null,
        isLast: false
    })

    /**
     * 打字中
     * @param content 内容
     */
    const typing = (content) => {
        messageInfo.value.body.content = content
    }

    /**
     * 更改上传类型
     * @param type 消息类型
     * @param body 消息体
     */
    const updateMessageType = (
        type: MessageTypeEnum,
        body: ChatMessageReq.MessageBody
    ) => {
        messageInfo.value.messageType = type
        messageInfo.value.body = body
    }

    /**
     * 增加emoji
     * @param emoji 表情
     */
    const addEmoji = (emoji: string) => {
        messageInfo.value.body.content = messageInfo.value.body.content + emoji
    }

    /**
     * 添加回复
     */
    const addReply = (message: TReplyMessage) => {
        messageInfo.value.replyMessageId = message.id
        replyMessage.value = message
    }

    /**
     * 取消回复
     */
    const cancelReply = () => {
        messageInfo.value.replyMessageId = null
        replyMessage.value = {
            id: null,
            name: null,
            content: null
        }
    }

    /**
     * 重置
     */
    const resetListPage = () => {
        listPage.value = {
            pageSize: 15,
            cursor: null,
            isLast: false
        }
    }

    /**
     * 添加消息
     */
    const addMessage = (data: ChatMessageResp.ChatMessageBaseResp) => {
        messageInfo.value.body.content = ""
        messageList.value.push(data)
        cancelReply()
    }

    /**
     * 初始化消息列表信息
     * @param list 消息列表
     */
    const initMessageList = (
        list: ChatMessageResp.ChatMessageBaseResp<any, any>[]
    ) => {
        messageList.value = list.reverse()
    }

    /**
     * 增加消息信息
     * @param list 消息
     */
    const updateMessageList = (
        list: ChatMessageResp.ChatMessageBaseResp<any, any>[]
    ) => {
        messageList.value.unshift(...list.reverse())
    }

    /**
     * 获取消息列表
     */
    const getMessageList = async () => {
        const result = await listMessage({
            pageSize: listPage.value.pageSize,
            cursor: listPage.value.cursor,
            roomId: sessionStore.sessionInfo.chattingId
        })
        if (result.data) {
            if (!listPage.value.cursor) {
                initMessageList(result.data.list)
            } else {
                updateMessageList(result.data.list)
            }
            listPage.value.isLast = result.data.isLast
            listPage.value.cursor = result.data.cursor
        }
    }

    watch(
        () => sessionStore.sessionInfo.chattingId,
        async () => {
            resetListPage()
            await getMessageList()
        },
        {
            deep: true,
            immediate: true
        }
    )

    return {
        messageInfo,
        messageList,
        replyMessage,
        listPage,
        typing,
        updateMessageType,
        addEmoji,
        addReply,
        cancelReply,
        addMessage,
        getMessageList
    }
})
