import { computed, ref, watch } from "vue"
import { defineStore } from "pinia"
import { RoomTypeEnum } from "@/enums/RoomTypeEnum"
import { Store } from "@/stores/store"
import { listMember } from "@/api/list"
import { ChatActiveEnums } from "@/enums/ChatActiveEnum"
import UserInfo = GlobalTypes.UserInfo

type TSessionInfo = {
    chattingId: number
    sessions: Store.SessionInfoType[]
    memberList: UserInfo[]
}

export const useSessionStore = defineStore("session", () => {
    const sessionInfo = ref<TSessionInfo>({
        chattingId: 1,
        sessions: [
            {
                id: 1,
                type: RoomTypeEnum.GROUP,
                name: "Blackchat全员大群聊",
                totalCount: 0,
                avatar: "https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJkQXoMolPPP0JVa8DF1kJ50nicQ1HJvYwXBoicBNVwlzlFNB23m0KCmd4AML7jE7icpwU7xCZJZ5pMA/132",
                lastMsgInfo: {
                    id: 1,
                    content: "你好，我是黑哥",
                    sendTime: "15:00",
                    sender: {
                        uid: 1,
                        name: "黑哥"
                    }
                }
            },
            {
                id: 2,
                type: RoomTypeEnum.GROUP,
                name: "Kkuil的群聊",
                totalCount: 0,
                avatar: "https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJkQXoMolPPP0JVa8DF1kJ50nicQ1HJvYwXBoicBNVwlzlFNB23m0KCmd4AML7jE7icpwU7xCZJZ5pMA/132",
                lastMsgInfo: {
                    id: 1,
                    content: "你好，我是黑哥",
                    sendTime: "15:00",
                    sender: {
                        uid: 1,
                        name: "黑哥"
                    }
                }
            }
        ],
        memberList: []
    })

    const listPage = ref<
        GlobalTypes.CursorPageReq & {
            activeStatus: number
            isLast: boolean
        }
    >({
        pageSize: 20,
        cursor: null,
        isLast: false,
        activeStatus: ChatActiveEnums.ONLINE
    })

    /**
     *  获取当前会话信息
     */
    const getSessionInfo: Store.SessionInfoType = computed(() => {
        return (
            sessionInfo.value.sessions.filter(
                (session) => session.id == sessionInfo.value.chattingId
            )[0] ?? {}
        )
    })

    /**
     * 切换会话
     * @param id 会话ID
     */
    const switchSession = (id: number) => {
        sessionInfo.value.memberList = []
        listPage.value.cursor = null
        listPage.value.isLast = false
        listPage.value.activeStatus = ChatActiveEnums.ONLINE
        sessionInfo.value.chattingId = id
        initSessionMemberList(sessionInfo.value.memberList)
    }

    /**
     * 初始化成员列表信息
     */
    const initSessionMemberList = (memberList: UserInfo[]) => {
        sessionInfo.value.memberList = memberList
    }

    /**
     * 增加成员信息
     * @param memberList 成员信息
     */
    const updateAddSessionMemberList = (memberList: UserInfo[]) => {
        sessionInfo.value.memberList.push(...memberList)
    }

    /**
     * 获取成员列表
     */
    const getMemberList = async () => {
        if (listPage.value.isLast) {
            return
        }
        const result = await listMember({
            pageSize: listPage.value.pageSize,
            cursor: listPage.value.cursor,
            roomId: sessionInfo.value.chattingId,
            activeStatus: listPage.value.activeStatus
        })
        if (result.data) {
            if (!listPage.value.cursor) {
                initSessionMemberList(result.data.list)
            } else {
                updateAddSessionMemberList(result.data.list)
            }
            listPage.value.cursor = result.data.cursor
            listPage.value.isLast = result.data.isLast
            listPage.value.activeStatus = result.data.extraInfo.activeStatus
            getSessionInfo.value.totalCount =
                result.data.extraInfo.totalCount || 0
        }
    }

    watch(
        () => sessionInfo.value.chattingId,
        async () => {
            await getMemberList()
        },
        {
            deep: false,
            immediate: true
        }
    )

    return {
        sessionInfo,
        listPage,
        updateAddSessionMemberList,
        initSessionMemberList,
        getSessionInfo,
        switchSession,
        getMemberList
    }
})
