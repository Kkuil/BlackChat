import { ref } from "vue"
import { defineStore } from "pinia"
import { ChatMessageReq } from "@/layout/components/chat/ChatContent/ChatMessageReq"

export const useMessageStore = defineStore("message", () => {
    const messageInfo = ref<ChatMessage.ChatMessageBaseReq<any>>({
        roomId: 1,
        messageType: 1,
        replyMessageId: 123,
        body: {
            content: "",
            atUidList: []
        }
    })

    /**
     * 打字中
     * @param replyMessageId 回复消息ID
     * @param content 内容
     * @param atUidList 艾特信息
     */
    const typing = ({ replyMessageId, content, atUidList }) => {
        messageInfo.value = {
            ...messageInfo.value,
            replyMessageId,
            body: {
                content,
                atUidList
            }
        }
    }

    /**
     * 发送成功
     */
    const sendSuccess = () => {
        messageInfo.value.body.content = ""
    }

    return { sessionInfo: messageInfo, typing, sendSuccess }
})
