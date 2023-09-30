<script setup lang="ts">
import ChatInput from "@/layout/components/chat/ChatContent/components/ChatInput.vue"
import ChatBody from "@/layout/components/chat/ChatContent/components/ChatBody.vue"
</script>

<template>
    <div
        class="w-full h-full flex flex-col bg-secondary rounded-[10px] sm:mx-[10px] p-[10px]"
    >
        <ChatBody class="flex-1 overflow-y-scroll mb-[10px]" />
        <ChatInput />
    </div>
</template>