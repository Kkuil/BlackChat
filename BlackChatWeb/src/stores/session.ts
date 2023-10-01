import { ref } from "vue"
import { defineStore } from "pinia"
import type { SessionInfoType } from "@/stores/store"
import { RoomTypeEnum } from "@/RoomTypeEnum"

type TSessionInfo = {
    chattingId: number
    sessions: SessionInfoType[]
}

export const useSessionStore = defineStore("session", () => {
    const sessionInfo = ref<TSessionInfo>({
        chattingId: 1,
        sessions: [
            {
                id: 1,
                type: RoomTypeEnum.GROUP,
                name: "Blackchat全员大群聊",
                avatar: "123",
                memberList: []
            }
        ]
    })

    return { sessionInfo }
})
