import { ref, watch } from "vue"
import { defineStore } from "pinia"
import { listMessage } from "@/api/list"
import { useSessionStore } from "@/stores/session"
import { MessageTypeEnum } from "@/enums/MessageTypeEnum"

const sessionStore = useSessionStore()

export const useMessageStore = defineStore("message", () => {
    const messageInfo = ref<ChatMessageReq.ChatMessageBaseReq<any>>({
        roomId: 1,
        messageType: MessageTypeEnum.TEXT,
        replyMessageId: null,
        body: {
            content: "",
            atUidList: [],
            urlContentMap: {}
        }
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
        messageInfo.value = {
            ...messageInfo.value,
            body: {
                ...messageInfo.value.body,
                content
            }
        }
    }

    /**
     * 增加emoji
     * @param emoji 表情
     */
    const addEmoji = (emoji: string) => {
        messageInfo.value = {
            ...messageInfo.value,
            body: {
                ...messageInfo.value.body,
                content: messageInfo.value.body.content + emoji
            }
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
     * @param roomId
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
            immediate: true
        }
    )

    return {
        messageInfo,
        messageList,
        listPage,
        typing,
        addEmoji,
        addMessage,
        getMessageList
    }
})
