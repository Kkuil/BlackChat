import { computed, ref } from "vue"
import { defineStore } from "pinia"
import { listMember, listSession } from "@/api/list"
import { ChatActiveEnums } from "@/enums/ChatActiveEnum"
import { RoomTypeEnum } from "@/enums/RoomTypeEnum"
import { updateUserInfoCache } from "@/utils/userCache"
import { getSessionByRoomId, pushReadMessage } from "@/api/session"
import eventBus from "@/utils/eventBus"
import { WsEventEnum } from "@/enums/websocket/WsEventEnum"
import { delAdmin, exitGroup, updateGroupInfo } from "@/api/group"
import { ElMessage } from "element-plus"
import { useUserStore } from "@/stores/user"
import { SHOW_IN_SESSION_MAP } from "@/constant/global"
import { GroupRoleEnum } from "@/enums/GroupRoleEnum"
import UserInfo = GlobalTypes.UserInfo

type TSessionInfo = {
    chattingId: number
    sessions: (Session & { totalCount: number })[]
    memberList: UserInfo[]
    totalCount: number
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

const userStore = useUserStore()

export const useSessionStore = defineStore("session", () => {
    const sessionInfo = ref<TSessionInfo>({
        chattingId: -99,
        sessions: [],
        memberList: [],
        totalCount: 0
    })

    const listMemberPage = ref<
        GlobalTypes.CursorPageReq & {
            activeStatus: number
            isLast: boolean
        }
    >({
        pageSize: 20,
        cursor: null,
        isLast: true,
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
    const switchSession = async (id: string) => {
        // 先判断当前列表中是否有该会话
        const index = sessionInfo.value.sessions.findIndex(
            (session) => session.roomId == id
        )
        if (index === -1) {
            const session = await getSessionInfoAsync(id)
            sessionInfo.value.chattingId = session.roomId
            sessionInfo.value.sessions.unshift(session)
        } else {
            sessionInfo.value.chattingId =
                sessionInfo.value.sessions[index].roomId
        }
        resetMemberList()
        // 获取群成员列表信息
        if (getSessionInfo.value.type == RoomTypeEnum.GROUP) {
            await getMemberList()
        }

        // 用户阅读信息上报
        if (getSessionInfo.value.unreadCount > 0) {
            await readMessage(sessionInfo.value.chattingId)
        }
    }

    /**
     * 异步获取会话信息
     * @param id 会话ID
     */
    const getSessionInfoAsync = async (id: string) => {
        // 获取会话信息
        const result = await getSessionByRoomId(id)
        if (result.data) {
            return result.data
        }
    }

    /**     成员     **/
    /**
     * 重置游标信息
     */
    const resetMemberList = () => {
        listMemberPage.value = {
            pageSize: 20,
            cursor: null,
            isLast: true,
            activeStatus: ChatActiveEnums.ONLINE
        }
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
            sessionInfo.value.totalCount = result.data.extraInfo.totalCount
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
            pageSize: 15,
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
        sessionInfo.value.sessions.push(...list)
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
     * 更新群名
     */
    const updateGroupNameHandler = async (groupName: string) => {
        const result = await updateGroupInfo({
            groupId: sessionInfo.value.chattingId,
            groupName
        })
        if (result.data) {
            ElMessage.success("更名成功")
            getSession(sessionInfo.value.chattingId).name = groupName
            return true
        }
    }

    /**
     *  获取当前会话信息
     */
    const getSessionInfo: Session = computed(() => {
        return (
            sessionInfo.value.sessions.find(
                (session) => session.roomId == sessionInfo.value.chattingId
            ) ?? {}
        )
    })

    /**
     * 获取当前未读总数
     */
    const getUnreadTotalCount: number = computed(() => {
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

    /**
     * 是否是热点群聊
     */
    const isHotFlag = computed(() => {
        return getSessionInfo.value.hotFlag === 1
    })

    /**
     * 当前角色
     */
    const currentRoleId = computed(() => {
        return sessionInfo.value.memberList.find(
            (member) => member.uid == userStore.userInfo.uid
        )?.roleId
    })

    /**
     * 当前管理员数量
     */
    const countAdmin = computed(() => {
        return sessionInfo.value.memberList.filter(
            (member) => member.roleId == GroupRoleEnum.ADMIN
        ).length
    })

    /**
     * 当前管理员
     */
    const listAdmin = computed(() => {
        return sessionInfo.value.memberList.filter(
            (member) => member.roleId == GroupRoleEnum.ADMIN
        )
    })

    /**
     * 设置管理员
     * @param uidList
     */
    const setAdmins = (uidList: number[]) => {
        sessionInfo.value.memberList.forEach((member) => {
            if (uidList.includes(member.uid)) {
                member.roleId = GroupRoleEnum.ADMIN
            }
        })
    }

    const delAdminHandler = async (uidList: number[]) => {
        if (uidList == null) {
            return
        }
        const result = await delAdmin({
            groupId: sessionInfo.value.chattingId,
            uidList
        })
        if (result.data) {
            sessionInfo.value.memberList.forEach((member) => {
                if (uidList.includes(member.uid)) {
                    member.roleId = GroupRoleEnum.MEMBER
                }
            })
            return true
        }
    }

    eventBus.on(WsEventEnum.SEND_MESSAGE, async ({ message }) => {
        // 更新当前会话的最新消息信息
        // 先判断当前列表中是否有该会话
        const index = sessionInfo.value.sessions.findIndex(
            (session) => session.roomId == message.message.roomId
        )
        if (index === -1) {
            const session = getSessionInfoAsync(message.message.roomId)
            sessionInfo.value.sessions.unshift(session)
        } else {
            const session = sessionInfo.value.sessions[index]
            sessionInfo.value.sessions.splice(index, 1)
            // 更新会话最新消息
            session.activeTime = message.message.sendTime
            console.log(
                userStore.getBaseInfoInCache(message.fromUser.uid).name +
                    ": " +
                    SHOW_IN_SESSION_MAP[message.message.type] ??
                    message.message.body.content
            )
            session.text =
                userStore.getBaseInfoInCache(message.fromUser.uid).name +
                ": " +
                (SHOW_IN_SESSION_MAP[message.message.type] ??
                    message.message.body.content)
            session.unreadCount += 1
            sessionInfo.value.sessions.unshift(session)
        }
    })

    eventBus.on(WsEventEnum.LOGIN_SUCCESS, async () => {
        resetSessionPage()
        sessionInfo.value.sessions = []
        await getSessionList()
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
        isHotFlag,
        currentRoleId,
        countAdmin,
        readMessage,
        switchSession,
        delAdminHandler,
        updateGroupNameHandler,
        listAdmin,
        setAdmins,
        getMemberList
    }
})
