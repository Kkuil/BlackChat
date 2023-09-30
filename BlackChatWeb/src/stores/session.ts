import { ref } from "vue"
import { defineStore } from "pinia"
import { ChatMessage } from "@/layout/components/chat/ChatContent/type"

export const useSessionStore = defineStore("session", () => {
    const sessionInfo = ref<ChatMessage.ChatMessageBaseReq<any>>({
        roomId: 1,
        messageType: 1,
        replyMessageId: 123,
        body: {
            content: "",
            atUidList: []
        }
    })

    const typing = ({ replyMessageId, content, atUidList }) => {
        sessionInfo.value = {
            ...sessionInfo.value,
            replyMessageId,
            body: {
                content,
                atUidList
            }
        }
    }

    return { sessionInfo, typing }
})
