import { computed, ref, watch } from "vue"
import { defineStore } from "pinia"
import { listMember, listSession } from "@/api/list"
import { ChatActiveEnums } from "@/enums/ChatActiveEnum"
import { RoomTypeEnum } from "@/enums/RoomTypeEnum"
import { updateUserInfoCache } from "@/utils/userCache"
import { pushReadMessage } from "@/api/contact"
import eventBus from "@/utils/eventBus"
import { WsEventEnum } from "@/enums/websocket/WsEventEnum"
import { exitGroup } from "@/api/group"
import { ElMessage } from "element-plus"
import UserInfo = GlobalTypes.UserInfo

type TSessionInfo = {
    chattingId: number
    sessions: (Session & { totalCount: number })[]
    memberList: UserInfo[]
}

export type Session = {
    roomId: number
    type: number
    hotFlag: number
    text: string
    name: string
    avatar: string
    activeTime: string
    unreadCount: number
}

export const useSessionStore = defineStore("session", () => {
    const sessionInfo = ref<TSessionInfo>({
        chattingId: -99,
        sessions: [],
        memberList: []
    })

    const listMemberPage = ref<
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
     * 通过房间ID获取会话信息
     * @param roomId
     */
    const getSession = (roomId: number) => {
        return sessionInfo.value.sessions.filter(
            (session) => session.roomId == roomId
        )[0]
    }

    /**
     * 切换会话
     * @param id 会话ID
     */
    const switchSession = (id: string) => {
        sessionInfo.value.chattingId = parseInt(id)
        initSessionMemberList()

        // 用户阅读信息上报
        if (getSessionInfo.value.unreadCount > 0) {
            readMessage(sessionInfo.value.chattingId)
        }
    }

    /**
     * 初始化成员列表信息
     */
    const initSessionMemberList = () => {
        listMemberPage.value = {
            pageSize: 20,
            cursor: null,
            isLast: false,
            activeStatus: ChatActiveEnums.ONLINE
        }
        sessionInfo.value.memberList = []
    }

    /**     成员     **/
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
        if (listMemberPage.value.isLast) {
            return
        }
        const result = await listMember({
            pageSize: listMemberPage.value.pageSize,
            cursor: listMemberPage.value.cursor,
            roomId: sessionInfo.value.chattingId,
            activeStatus: listMemberPage.value.activeStatus
        })
        if (result.data) {
            if (!listMemberPage.value.cursor) {
                initSessionMemberList(result.data.list)
            } else {
                updateAddSessionMemberList(result.data.list)
            }
            listMemberPage.value.cursor = result.data.cursor
            listMemberPage.value.isLast = result.data.isLast
            listMemberPage.value.activeStatus =
                result.data.extraInfo.activeStatus
            getSessionInfo.value.totalCount =
                result.data.extraInfo.totalCount || 0
            const list = result.data.list.map((member) => {
                const user: UserInfo = {
                    uid: member.uid,
                    name: member.name,
                    avatar: member.avatar
                }
                return user
            })
            updateUserInfoCache(list)
        }
    }

    /**
     * 用户阅读信息上报
     * @param roomId
     */
    const readMessage = async (roomId: number) => {
        const result = await pushReadMessage({
            roomId
        })
        if (result.data) {
            getSession(roomId).unreadCount = 0
        }
    }

    /**    会话   **/
    const listSessionPage = ref<
        GlobalTypes.CursorPageReq & { isLast: boolean }
    >({
        pageSize: 10,
        cursor: null,
        isLast: false
    })

    /**
     * 重置游标信息
     */
    const resetSessionPage = () => {
        listSessionPage.value = {
            pageSize: 10,
            cursor: null,
            isLast: false
        }
    }

    /**
     * 初始化会话列表信息
     * @param list 会话列表
     */
    const initSessionList = (list: Session<any, any>[]) => {
        sessionInfo.value.sessions = list
        if (sessionInfo.value.chattingId < 0) {
            sessionInfo.value.chattingId = list[0].roomId
        }
    }

    /**
     * 增加会话信息
     * @param list 会话
     */
    const updateSessionList = (list: Session<any, any>[]) => {
        sessionInfo.value.sessions.unshift(...list)
    }

    /**
     * 获取会话列表
     */
    const getSessionList = async () => {
        const result = await listSession({
            pageSize: listSessionPage.value.pageSize,
            cursor: listSessionPage.value.cursor
        })
        if (result.data) {
            if (!listSessionPage.value.cursor) {
                initSessionList(result.data.list)
            } else {
                updateSessionList(result.data.list)
            }
            listSessionPage.value.isLast = result.data.isLast
            listSessionPage.value.cursor = result.data.cursor
        }
    }

    /**
     * 退出群聊
     */
    const exitGroupRoom = async () => {
        const result = await exitGroup(sessionInfo.value.chattingId)
        if (result.data) {
            // 删除会话
            const index = sessionInfo.value.sessions.findIndex(
                (session) => session.roomId === sessionInfo.value.chattingId
            )
            sessionInfo.value.sessions.splice(index, 1)
            sessionInfo.value.chattingId = sessionInfo.value.sessions[0]
            ElMessage.success(result.message)
        }
    }

    /**
     *  获取当前会话信息
     */
    const getSessionInfo: Session = computed(() => {
        return (
            sessionInfo.value.sessions.filter(
                (session) => session.roomId == sessionInfo.value.chattingId
            )[0] ?? {}
        )
    })

    /**
     * 获取当前未读总数
     */
    const getUnreadTotalCount = computed(() => {
        return sessionInfo.value.sessions.reduce((total, session) => {
            return total + session.unreadCount
        }, 0)
    })

    /**
     * 判断是否是群聊
     */
    const isGroup = computed(() => {
        return getSessionInfo.value.type === RoomTypeEnum.GROUP
    })

    watch(
        () => sessionInfo.value.chattingId,
        async () => {
            // 获取群成员列表信息
            if (getSessionInfo.value.type == RoomTypeEnum.GROUP) {
                await getMemberList()
            }
        }
    )

    eventBus.on(WsEventEnum.SEND_MESSAGE, ({ message }) => {
        // 更新当前会话的最新消息信息
        const index = sessionInfo.value.sessions.findIndex(
            (session) => session.roomId === sessionInfo.value.chattingId
        )
        const session = sessionInfo.value.sessions[index]
        session.activeTime = message.message.sendTime
        sessionInfo.value.sessions.splice(index, 1)
        sessionInfo.value.sessions.unshift(session)
    })

    return {
        sessionInfo,
        listSessionPage,
        resetSessionPage,
        listMemberPage,
        getSessionInfo,
        getUnreadTotalCount,
        exitGroupRoom,
        getSessionList,
        isGroup,
        readMessage,
        switchSession,
        getMemberList
    }
})
