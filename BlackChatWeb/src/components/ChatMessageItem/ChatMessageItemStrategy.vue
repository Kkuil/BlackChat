<script setup lang="ts">
import { ChatMessageItemTypes } from "@/components/ChatMessageItem/type"
import ChatMessageItem from "@/components/ChatMessageItem/components/ChatMessageItem.vue"
import ChatSystemItem from "@/components/ChatMessageItem/components/ChatSystemItem.vue"

defineProps<ChatMessageItemTypes.ChatItemStrategyType>()
</script>

<template>
    <ChatMessageItem
        v-if="type === 'text'"
        :avatar="configInfo.avatar"
        :username="configInfo.username"
        :message="configInfo.message"
        :direction="'left'"
        :send-time="configInfo.sendTime"
    />
    <ChatSystemItem v-if="type === 'system'" :message="configInfo.message" />
</template>