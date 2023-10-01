<script setup lang="ts">
import { onMounted, ref } from "vue"
import eventBus from "@/utils/eventBus"
import { WsEventEnum } from "@/enums/websocket/WsEventEnum"
import SvgIcon from "@/components/SvgIcon/SvgIcon.vue"
import { useSessionStore } from "@/stores/session"
import CursorPage = GlobalTypes.CursorPage

type TMember = {
    uid: number
    name: string
    avatar: string
    status: number
}

const sessionStore = useSessionStore()
const member = ref<TMember[]>([])

const listPage = ref<CursorPage<number>>({
    pageSize: 20,
    cursor: null,
    data: sessionStore.sessionInfo.chattingId
})

onMounted(() => {
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

sessionStore.$subscribe((mutation, state) => {
    console.log(mutation, state)
})
</script>

<template>
    <div
        class="online-list flex flex-col ml-[10px] bg-secondary rounded-[10px] text-[#fff] p-[10px]"
    >
        <h1 class="online-count flex items-center h-[30px] text-[14px]">
            在线人数：{{ member.length }}
        </h1>
        <ul class="flex-1 overflow-y-scroll" v-if="member.length">
            <li
                class="item flex items-center py-[7px] cursor-pointer mb-[5px] hover:bg-[#263242] rounded-[5px] px-[7px]"
                v-for="m in member"
                :key="m.uid"
            >
                <el-badge>
                    <el-avatar :size="'small'" :src="m.avatar" />
                </el-badge>
                <span class="text-[14px] ml-[5px]">{{ m.name }}</span>
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

<style scoped></style>