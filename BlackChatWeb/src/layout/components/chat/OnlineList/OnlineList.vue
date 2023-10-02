<script setup lang="ts">
import { onMounted, ref } from "vue"
import eventBus from "@/utils/eventBus"
import { WsEventEnum } from "@/enums/websocket/WsEventEnum"
import SvgIcon from "@/components/SvgIcon/SvgIcon.vue"
import { useSessionStore } from "@/stores/session"
import { listMember } from "@/api/chat/list"
import { ChatActiveEnums } from "@/enums/ChatActiveEnum"
import CursorPageReq = GlobalTypes.CursorPageReq

const sessionStore = useSessionStore()

const listPage = ref<CursorPageReq & { roomId: number; activeStatus: number }>({
    pageSize: 20,
    cursor: null,
    isLast: false,
    roomId: sessionStore.sessionInfo.chattingId,
    activeStatus: ChatActiveEnums.ONLINE
})

onMounted(() => {
    getMemberList()
    initListeners()
})

/**
 * 初始化监听器
 */
const initListeners = () => {
    eventBus.on(
        WsEventEnum.UPDATE_ONLINE_LIST,
        (data: { uid: number; status: number }) => {
            console.log(data)
        }
    )
}

/**
 * 获取成员列表
 */
const getMemberList = async () => {
    if (listPage.value.isLast) {
        return
    }
    const result = await listMember(listPage.value)
    if (result.data) {
        if (!listPage.value.cursor) {
            sessionStore.initSessionMemberList(result.data.list)
        } else {
            sessionStore.updateAddSessionMemberList(result.data.list)
        }
        listPage.value.cursor = result.data.cursor
        listPage.value.isLast = result.data.isLast
        listPage.value.activeStatus = <number>(
            result.data.list[result.data.list.length - 1].activeStatus
        )
    }
}
</script>

<template>
    <div
        class="online-list flex flex-col ml-[10px] bg-secondary rounded-[10px] text-[#fff] p-[10px]"
    >
        <h1 class="online-count flex items-center h-[30px] text-[14px]">
            在线人数：{{ sessionStore.getSessionInfo().memberList.length }}
        </h1>
        <ul
            class="list flex-1 overflow-y-auto"
            v-infinite-scroll="getMemberList"
            v-if="sessionStore.getSessionInfo().memberList.length"
        >
            <li
                class="item flex items-center py-[7px] cursor-pointer mb-[5px] hover:bg-[#263242] rounded-[5px] px-[7px]"
                v-for="member in sessionStore.getSessionInfo().memberList"
                :key="member.uid"
            >
                <el-badge
                    is-dot
                    :type="member.activeStatus === 1 ? 'primary' : 'warning'"
                >
                    <el-avatar :size="'small'" :src="member.avatar" />
                </el-badge>
                <div
                    class="w-full line-clamp-1 overflow-hidden overflow-ellipsis text-[14px] ml-[5px]"
                >
                    {{ member.name }}
                </div>
            </li>
            <li
                v-if="listPage.isLast"
                class="w-full flex-center text-[#5b6061] text-[12px]"
            >
                暂无更多成员
            </li>
            <li
                class="w-full flex-center"
                v-observe="getMemberList"
                v-if="!listPage.isLast"
            >
                <i class="iconfont icon-loading animate-spin"></i>
            </li>
        </ul>
        <el-empty class="w-full flex-1 flex-center" v-else>
            <template #image>
                <div class="w-full h-full flex-center">
                    <svg-icon icon-class="group" class="text-[50px]" />
                </div>
            </template>
            <template #description>
                <div class="font-serif text-[13px]">
                    本房间还没有成员，去邀请一个吧~
                </div>
                <ElButton type="primary" class="mt-[10px]" size="small">
                    邀请朋友
                </ElButton>
            </template>
        </el-empty>
    </div>
</template>

<style scoped>
.online-list .list {
    --el-color-primary: #adff2f;
    --el-color-warning: #ccc;
}

.online-list .el-badge__content {
    border: 0;
}
</style>