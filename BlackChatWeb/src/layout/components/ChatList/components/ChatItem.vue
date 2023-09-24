<script setup lang="ts">
import { ChatItem } from "@/layout/components/ChatList/components/type"

defineProps<ChatItem.ChatItemPropsType>()
</script>

<template>
    <div
        class="w-full h-[65px] rounded-[10px] bg-secondary cursor-pointer hover:bg-opacity-80 flex items-center px-[10px] hover:bg-[#263343] transition-[background-color]"
    >
        <el-avatar :src="avatar" />
        <div class="w-full flex flex-1 flex-col ml-[10px] text-[#fff]">
            <div class="text-[15px] flex-[1/2]">{{ name }}</div>
            <div class="text-[13px] flex-[1/2] line-clamp-1">
                {{ message }}
            </div>
        </div>
    </div>
</template>

<style scoped lang="scss">
.selected {
    background-color: #263343 !important;
}
</style>