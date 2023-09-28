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
        <el-avatar
            :src="avatar"
            :title="username"
            :size="35"
            class="cursor-pointer"
        >
            <el-icon :size="20">
                <Avatar />
            </el-icon>
        </el-avatar>
        <div
            class="flex flex-col justify-between items-start"
            :class="direction == 'left' ? 'ml-[7px]' : 'mr-[7px]'"
        >
            <div class="top text-[#777]">
                <span class="username text-[13px]">{{ username }}</span>
                <span
                    class="send-time text-[12px] ml-[5px] opacity-0 transition-[opacity]"
                    >{{ sendTime }}</span
                >
            </div>
            <div
                class="message p-[5px] rounded-[5px] bg-[#f5f5f5] text-[14px] flex-shrink-0"
            >
                {{ message }}
            </div>
        </div>
    </div>
</template>

<style scoped lang="scss">
.top {
    &:hover {
        .send-time {
            opacity: 1 !important;
        }
    }
}
</style>