<script setup lang="ts">
import ChatInput from "@/layout/components/ChatContent/components/ChatInput.vue"
import ChatBody from "@/layout/components/ChatContent/components/ChatBody.vue"
</script>

<template>
    <div
        class="chat-content w-full flex flex-col bg-secondary rounded-[10px] mx-[10px] p-[10px]"
    >
        <ChatBody class="flex-[94.5%]" />
        <ChatInput class="flex-[6.5%]" />
    </div>
</template>

<style scoped></style>