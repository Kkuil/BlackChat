<script setup lang="ts">
import ChatInput from "@/layout/components/chat/ChatContent/components/ChatInput.vue"
import ChatBody from "@/layout/components/chat/ChatContent/components/ChatBody.vue"
import { useSessionStore } from "@/stores/session"

const sessionStore = useSessionStore()
</script>

<template>
    <div
        class="w-full h-full flex flex-col bg-secondary rounded-[10px] overflow-hidden"
    >
        <div
            class="w-full h-[8%] bg-third flex items-center justify-between px-[10px] text-[#f5f5f5] font-extrabold"
        >
            <h1>{{ sessionStore.getSessionInfo.name }}</h1>
            <el-button
                type="danger"
                size="small"
                v-if="sessionStore.getSessionInfo.hotFlag !== 1"
            >
                退出群聊
            </el-button>
        </div>
        <div class="px-[15px] py-[10px] h-[92%] flex flex-col">
            <ChatBody class="flex-1 overflow-y-scroll mb-[10px]" />
            <ChatInput />
        </div>
    </div>
</template>