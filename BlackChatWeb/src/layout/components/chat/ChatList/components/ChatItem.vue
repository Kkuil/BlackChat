<script setup lang="ts">
import { Avatar } from "@element-plus/icons-vue"
import type { Session } from "@/stores/session"
import { formatTimestamp } from "@/utils/TimeUtil"

defineProps<{
    session: Session
}>()
</script>

<template>
    <div class="w-full h-[65px] flex items-center px-[10px]">
        <el-badge
            :value="session.unreadCount"
            :hidden="!session.unreadCount"
            :max="99"
        >
            <el-avatar :src="session.avatar">
                <el-icon :size="25">
                    <Avatar />
                </el-icon>
            </el-avatar>
        </el-badge>
        <div class="w-[62%] flex flex-col ml-[10px] text-[#fff]">
            <div class="text-[15px] flex-[1/2]">{{ session.name }}</div>
            <div
                class="w-full text-[13px] flex-[1/2] whitespace-nowrap overflow-hidden overflow-ellipsis"
            >
                {{ session.text }}
            </div>
        </div>
        <div
            class="last-opt-time xl:flex justify-end hidden w-[20%] flex-center text-[8px] text-[#777] whitespace-nowrap"
        >
            {{
                session.activeTime
                    ? formatTimestamp(new Date(session.activeTime))
                    : ""
            }}
        </div>
    </div>
</template>

<style scoped lang="scss">
.selected {
    background-color: #263343 !important;
}
</style>