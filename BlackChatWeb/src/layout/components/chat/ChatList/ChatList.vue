<script setup lang="ts">
import ChatItem from "@/layout/components/chat/ChatList/components/ChatItem.vue"
import { useSessionStore } from "@/stores/session"

const sessionStore = useSessionStore()

/**
 * 切换会话
 * @param e
 */
const switchSession = (e: Event & { target: { dataset: { id: string } } }) => {
    const id = e.target.dataset.id
    console.log(id, sessionStore.sessionInfo.chattingId)
    if (!id || id == sessionStore.sessionInfo.chattingId) return
    sessionStore.switchSession(id)
}
</script>

<template>
    <div class="chat-list overflow-y-scroll pr-[10px]" @click="switchSession">
        <div
            v-for="session in sessionStore.sessionInfo.sessions"
            :key="session.roomId"
            class="transition-[background-color] rounded-[10px] cursor-pointer item"
            :class="
                session.roomId == sessionStore.sessionInfo.chattingId
                    ? 'bg-[#2c3e50] hover:bg-[#2c3e50]'
                    : 'bg-[#202124] hover:bg-[#484d50]'
            "
            :data-id="session.roomId"
        >
            <ChatItem
                :session="session"
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