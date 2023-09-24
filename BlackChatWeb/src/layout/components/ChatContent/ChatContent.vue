<script setup lang="ts">
import ChatInput from "@/layout/components/ChatContent/components/ChatInput.vue"
import ChatBody from "@/layout/components/ChatContent/components/ChatBody.vue"
</script>

<template>
    <div
        class="w-full h-full flex flex-col bg-secondary rounded-[10px] sm:mx-[10px] p-[10px]"
    >
        <ChatBody
            class="flex-[93.5%] sm:flex-[93.5%] overflow-y-scroll mb-[10px]"
        />
        <ChatInput class="flex-[6.5%] sm:flex-[6.5%]" />
    </div>
</template>