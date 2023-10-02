import { ref } from "vue"
import { defineStore } from "pinia"
import { RoomTypeEnum } from "@/enums/RoomTypeEnum"
import { Store } from "@/stores/store"
import UserInfo = GlobalTypes.UserInfo

type TSessionInfo = {
    chattingId: number
    sessions: Store.SessionInfoType[]
}

export const useSessionStore = defineStore("session", () => {
    const sessionInfo = ref<TSessionInfo>({
        chattingId: 2,
        sessions: [
            {
                id: 2,
                type: RoomTypeEnum.GROUP,
                name: "Blackchat全员大群聊",
                avatar: "",
                memberList: []
            }
        ]
    })

    /**
     *  获取当前会话信息
     */
    const getSessionInfo = () => {
        return sessionInfo.value.sessions.filter(
            (session) => session.id === sessionInfo.value.chattingId
        )[0]
    }

    /**
     * 初始化成员列表信息
     */
    const initSessionMemberList = (memberList: UserInfo[]) => {
        sessionInfo.value.sessions.forEach((session) => {
            if (session.id === sessionInfo.value.chattingId) {
                session.memberList = memberList
                return
            }
        })
    }

    /**
     * 增加成员信息
     * @param memberList 成员信息
     */
    const updateAddSessionMemberList = (memberList: UserInfo[]) => {
        sessionInfo.value.sessions.forEach((session) => {
            if (session.id === sessionInfo.value.chattingId) {
                session.memberList.push(...memberList)
                return
            }
        })
    }

    return {
        sessionInfo,
        updateAddSessionMemberList,
        initSessionMemberList,
        getSessionInfo
    }
})
