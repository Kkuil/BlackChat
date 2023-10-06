<script setup lang="ts">
import { ChatItem } from "@/layout/components/chat/ChatList/components/type"
import { Avatar } from "@element-plus/icons-vue"

defineProps<ChatItem.ChatItemPropsType>()
</script>

<template>
    <div class="w-full h-[65px] flex items-center px-[10px]">
        <el-avatar :src="avatar">
            <el-icon :size="25">
                <Avatar />
            </el-icon>
        </el-avatar>
        <div class="w-[70%] flex flex-col ml-[10px] text-[#fff]">
            <div class="text-[15px] flex-[1/2]">{{ name }}</div>
            <div
                class="w-full text-[13px] flex-[1/2] whitespace-nowrap overflow-hidden overflow-ellipsis"
            >
                {{ message }}
            </div>
        </div>
        <div class="last-opt-time w-[10%] flex-center text-[13px] text-[#777]">
            {{ extraInfo.sendTime }}
        </div>
    </div>
</template>

<style scoped lang="scss">
.selected {
    background-color: #263343 !important;
}
</style>