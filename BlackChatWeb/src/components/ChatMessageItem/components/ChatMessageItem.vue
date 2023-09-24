<script setup lang="ts">
import { ChatMessageItemTypes } from "@/components/ChatMessageItem/type"
import { computed } from "vue"
import { Avatar } from "@element-plus/icons-vue"

const props = defineProps<ChatMessageItemTypes.ChatMessageItemPropsType>()

/**
 * 计算当前消息的布局方向
 */
const computedDirection = computed(() => {
    switch (props.direction) {
        case "left":
            return "row"
        case "right":
            return "row-reverse"
        default:
            return "row"
    }
})
</script>

<template>
    <div
        :class="`message-item py-[10px] flex items-center flex-${computedDirection}`"
    >
        <el-avatar :src="avatar" :title="username" :size="35">
            <el-icon :size="20">
                <Avatar />
            </el-icon>
        </el-avatar>
        <div
            class="flex flex-col justify-between right"
            :class="direction == 'left' ? 'ml-[7px]' : 'mr-[7px]'"
        >
            <span class="text-[12px] text-[#777]">{{ username }}</span>
            <div class="message p-[5px] rounded-[5px] bg-[#f5f5f5] text-[15px]">
                {{ message }}
            </div>
        </div>
    </div>
</template>

<style scoped lang="scss"></style>