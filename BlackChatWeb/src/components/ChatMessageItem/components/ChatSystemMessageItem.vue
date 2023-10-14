<script setup lang="ts">
import SystemMessageBody = ChatMessageResp.SystemMessageBody
import type { ChatMessageResp } from "@/layout/components/chat/ChatContent/ChatMessageResp.d.ts"

defineProps<{
    message: ChatMessageResp.ChatMessageBaseResp<SystemMessageBody, any>
}>()
</script>

<template>
    <div class="w-full my-[10px] system flex-center text-[#777] text-xs">
        {{ message.message.body.content }}
    </div>
</template>

<style scoped lang="scss"></style>