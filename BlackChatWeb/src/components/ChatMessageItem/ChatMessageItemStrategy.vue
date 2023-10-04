<script setup lang="ts">
import ChatCommonMessageItem from "@/components/ChatMessageItem/components/ChatCommonMessageItem.vue"
import ChatSystemMessageItem from "@/components/ChatMessageItem/components/ChatSystemMessageItem.vue"
import { useUserStore } from "@/stores/user"
import { MessageTypeEnum } from "@/enums/MessageTypeEnum"

defineProps<{ message: ChatMessageResp.ChatMessageBaseResp<any, any> }>()

const userStore = useUserStore()
</script>

<template>
    <ChatCommonMessageItem
        v-if="message.message.type != MessageTypeEnum.RECALL"
        :direction="
            userStore.userInfo.uid == message.fromUser.uid ? 'right' : 'left'
        "
        :message="message"
    />
    <ChatSystemMessageItem v-else :message="message" />
</template>