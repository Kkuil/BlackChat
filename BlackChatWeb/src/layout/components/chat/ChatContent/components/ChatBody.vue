<script setup lang="ts">
import ChatMessageItemStrategy from "@/components/ChatMessageItem/ChatMessageItemStrategy.vue"
import { MessageTypeEnum } from "@/enums/MessageTypeEnum"
import { useMessageStore } from "@/stores/message"
import { ref } from "vue"
import type { ChatMessageResp } from "@/layout/components/chat/ChatContent/ChatMessageResp.d.ts"
import SystemMessageBody = ChatMessageResp.SystemMessageBody

const messageStore = useMessageStore()
const bodyRef = ref<HTMLDivElement>()
const lastMessage: ChatMessageResp.ChatMessageBaseResp<SystemMessageBody, any> =
    {
        fromUser: {
            uid: "10003"
        },
        message: {
            id: 3170000,
            sendTime: "20210317",
            type: MessageTypeEnum.SYSTEM,
            body: {
                content: "已到达顶部"
            }
        }
    }
</script>

<template>
    <div class="h-full bg-secondary overflow-x-hidden" ref="bodyRef">
        <span
            v-observe="messageStore.getMessageList"
            v-if="!messageStore.listPage.isLast"
        >
        </span>
        <ChatMessageItemStrategy
            v-if="messageStore.listPage.isLast"
            :message="lastMessage"
        />
        <ChatMessageItemStrategy
            v-for="message in messageStore.messageList"
            :key="message?.message?.id"
            :message="message"
        />
    </div>
</template>

<style scoped lang="scss"></style>