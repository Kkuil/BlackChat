import { ref, watch } from "vue"
import { defineStore } from "pinia"
import { listMessage } from "@/api/list"
import { useSessionStore } from "@/stores/session"
import { MessageTypeEnum } from "@/enums/MessageTypeEnum"
import eventBus from "@/utils/eventBus"
import { WsEventEnum } from "@/enums/websocket/WsEventEnum"
import { ChatMessageResp } from "@/layout/components/chat/ChatContent/ChatMessageResp"
import { updateByUidList } from "@/utils/userCache"
import { useUserStore } from "@/stores/user"

const sessionStore = useSessionStore()

export type TReplyMessage = {
    id: number
    name: string
    content: string
}

const userStore = useUserStore()

export const useMessageStore = defineStore("message", () => {
    // 消息信息
    const messageInfo = ref<ChatMessageReq.ChatMessageBaseReq<any>>({
        messageType: MessageTypeEnum.TEXT,
        replyMessageId: null,
        body: {
            content: "",
            atUidList: []
        }
    })

    // 消息回复
    const replyMessage = ref<TReplyMessage>({
        id: null,
        name: null,
        content: null
    })

    // 消息列表
    const messageList = ref<ChatMessageResp.ChatMessageBaseResp<any, any>[]>([])

    // 消息列表分页
    const listPage = ref<
        GlobalTypes.CursorPageReq & {
            isLast: boolean
        }
    >({
        pageSize: 15,
        cursor: null,
        isLast: true
    })

    /**
     * 打字中
     * @param content 内容
     */
    const typing = (content: string) => {
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
     * 添加艾特
     */
    const addAite = ({ uid, name }: { uid: number; name: string }) => {
        messageInfo.value.body.atUidList.push([
            ...new Set([...messageInfo.value.body.atUidList, uid])
        ])
        messageInfo.value.body.content += "@" + name + " "
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
     * 重置游标
     */
    const resetListPage = () => {
        listPage.value = {
            pageSize: 15,
            cursor: null,
            isLast: true
        }
    }

    /**
     * 重置消息类型
     */
    const resetMessage = () => {
        messageInfo.value.messageType = MessageTypeEnum.TEXT
        messageInfo.value.body = {
            content: "",
            atUidList: []
        }
    }

    /**
     * 添加消息
     */
    const updateMessage = (data: ChatMessageResp.ChatMessageBaseResp) => {
        // 如果是当前会话收到了消息就更新消息列表
        if (data.message.roomId === sessionStore.sessionInfo.chattingId) {
            if (data.message.type === MessageTypeEnum.REVOKE) {
                const index = messageList.value.findIndex(
                    (item) => item.message.id == data.message.id
                )
                messageList.value[index].message.type = MessageTypeEnum.REVOKE
                messageList.value[index].message.body.content =
                    data.message.content
            } else {
                messageList.value.push(data)
                if (userStore.userInfo.uid == data.fromUser.uid) {
                    cancelReply()
                    resetMessage()
                }
            }
        }
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
            console.log("result.data.list", result.data.list)
            refreshCache(result.data.list)
        }
    }

    /**
     * 刷新缓存
     * @param list 刷新ID列表
     */
    const refreshCache = async (
        list: ChatMessageResp.ChatMessageBaseResp<any, any>[]
    ) => {
        const needGetLUidList = []
        list.forEach((message) => {
            if (!userStore.userInfoCache[message.fromUser.uid + ""]) {
                needGetLUidList.push(message.fromUser.uid)
            }
        })
        if (needGetLUidList.length) {
            await updateByUidList(needGetLUidList)
        }
    }

    watch(
        () => sessionStore.sessionInfo.chattingId,
        async () => {
            resetListPage()
            await getMessageList()
        }
    )

    eventBus.on(WsEventEnum.SEND_MESSAGE, ({ message }) => {
        updateMessage(message)
    })

    return {
        messageInfo,
        messageList,
        replyMessage,
        listPage,
        typing,
        updateMessageType,
        addEmoji,
        addReply,
        addAite,
        cancelReply,
        resetMessage,
        addMessage: updateMessage,
        getMessageList
    }
})
