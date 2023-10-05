<script setup lang="ts">
import ChatMessageItemStrategy from "@/components/ChatMessageItem/ChatMessageItemStrategy.vue"
import { MessageTypeEnum } from "@/enums/MessageTypeEnum"
import { useMessageStore } from "@/stores/message"
import SystemMessageBody = ChatMessageResp.SystemMessageBody

const messageStore = useMessageStore()

const firstMessage: ChatMessageResp.ChatMessageBaseResp<
    SystemMessageBody,
    any
> = {
    fromUser: {
        uid: "10003",
        name: "Kkuil",
        avatar: "https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJkQXoMolPPP0JVa8DF1kJ50nicQ1HJvYwXBoicBNVwlzlFNB23m0KCmd4AML7jE7icpwU7xCZJZ5pMA/132"
    },
    message: {
        id: 3170000,
        sendTime: "20210317",
        type: MessageTypeEnum.RECALL,
        body: {
            content: "已到达顶部"
        }
    }
}
</script>

<template>
    <div class="h-full bg-secondary" ref="bodyRef">
        <span
            v-observe="messageStore.getMessageList"
            v-if="!messageStore.listPage.isLast"
        >
        </span>
        <ChatMessageItemStrategy
            v-if="messageStore.listPage.isLast"
            :message="firstMessage"
        />
        <ChatMessageItemStrategy
            v-for="message in messageStore.messageList"
            :key="message.message.id"
            :message="message"
        />
    </div>
</template>

<style scoped lang="scss"></style>