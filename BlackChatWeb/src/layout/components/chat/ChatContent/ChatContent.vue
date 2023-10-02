<script setup lang="ts">
import ChatInput from "@/layout/components/chat/ChatContent/components/ChatInput.vue"
import ChatBody from "@/layout/components/chat/ChatContent/components/ChatBody.vue"
import { useSessionStore } from "@/stores/session"

const sessionStore = useSessionStore()
</script>

<template>
    <div
        class="w-full h-full flex flex-col bg-secondary rounded-[10px] sm:mx-[10px] overflow-hidden"
    >
        <div
            class="w-full flex-[8%] bg-third hidden sm:flex items-center px-[10px] text-[#f5f5f5] font-extrabold"
        >
            {{ sessionStore.getSessionInfo().name }}
        </div>
        <div class="p-[10px] flex-[92%] flex flex-col">
            <ChatBody class="flex-1 overflow-y-scroll mb-[10px]" />
            <ChatInput />
        </div>
    </div>
</template>