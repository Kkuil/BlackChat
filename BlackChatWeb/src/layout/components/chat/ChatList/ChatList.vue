<script setup lang="ts">
import ChatItem from "@/layout/components/chat/ChatList/components/ChatItem.vue"
import { useSessionStore } from "@/stores/session"

const sessionStore = useSessionStore()

/**
 * 切换会话
 * @param e
 */
const switchSession = (e: Event & { target: { dataset: { id: number } } }) => {
    const id = e.target.dataset.id
    if (!id) return
    sessionStore.switchSession(id)
}
</script>

<template>
    <div class="chat-list overflow-y-scroll pr-[10px]" @click="switchSession">
        <div
            v-for="session in sessionStore.sessionInfo.sessions"
            :key="session.id"
            class="transition-[background-color] rounded-[10px] cursor-pointer item"
            :class="
                session.id == sessionStore.sessionInfo.chattingId
                    ? 'bg-[#090c10] hover:bg-[#090c10]'
                    : 'bg-[#202124] hover:bg-[#090c10]'
            "
            :data-id="session.id"
        >
            <ChatItem
                :avatar="session.avatar"
                :name="session.name"
                :message="`${session.lastMsgInfo.sender.name}: ${session.lastMsgInfo.content}`"
                :extra-info="{
                    sendTime: session.lastMsgInfo.sendTime
                }"
                class="mb-[10px] pointer-events-none"
            />
        </div>
    </div>
</template>

<style scoped lang="scss">
.chat-list {
    .item:last-child {
        margin-bottom: 0 !important;
    }
}
</style>