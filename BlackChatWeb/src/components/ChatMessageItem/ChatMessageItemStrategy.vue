<script setup lang="ts">
import ChatCommonMessageItem from "@/components/ChatMessageItem/components/ChatCommonMessageItem.vue"
import ChatSystemMessageItem from "@/components/ChatMessageItem/components/ChatSystemMessageItem.vue"
import { useUserStore } from "@/stores/user"
import { MessageTypeEnum } from "@/enums/MessageTypeEnum"

defineProps<{ message: ChatMessageResp.ChatMessageBaseResp<any, any> }>()

const userStore = useUserStore()
</script>

<template>
    <Transition v-if="message?.message?.type != MessageTypeEnum.SYSTEM">
        <ChatCommonMessageItem
            :direction="
                userStore.userInfo.uid == message?.fromUser?.uid
                    ? 'right'
                    : 'left'
            "
            :message="message"
        />
    </Transition>
    <ChatSystemMessageItem v-else :message="message" />
</template>

<style scoped lang="scss">
/* 1. 声明过渡效果 */
.fade-move,
.fade-enter-active,
.fade-leave-active {
    transition: all 0.5s cubic-bezier(0.55, 0, 0.1, 1);
}

/* 2. 声明进入和离开的状态 */
.fade-enter-from,
.fade-leave-to {
    opacity: 0;
    transform: scaleY(0.01) translate(30px, 0);
}

/* 3. 确保离开的项目被移除出了布局流
      以便正确地计算移动时的动画效果。 */
.fade-leave-active {
    position: absolute;
}
</style>