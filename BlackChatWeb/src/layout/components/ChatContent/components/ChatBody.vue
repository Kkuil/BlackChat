<script setup lang="ts">
import ChatMessageItemStrategy from "@/components/ChatMessageItem/ChatMessageItemStrategy.vue"
import { ChatMessageItemTypes } from "@/components/ChatMessageItem/type"

const configInfo: ChatMessageItemTypes.ChatMessageItemPropsType = {
    avatar: "123",
    username: "Kkuil",
    message: "123",
    sendTime: "2023/9/24  12:30:00"
}
</script>

<template>
    <div class="h-full bg-secondary">
        <ChatMessageItemStrategy
            v-for="index in 30"
            :key="index"
            :type="index % 2 ? 'text' : 'system'"
            :config-info="configInfo"
        />
    </div>
</template>

<style scoped lang="scss"></style>