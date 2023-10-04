<script setup lang="ts">
import SystemMessageBody = ChatMessageResp.SystemMessageBody

defineProps<{
    message: ChatMessageResp.ChatMessageBaseResp<SystemMessageBody, any>
}>()
</script>

<template>
    <div class="system flex-center text-[#777] text-xs">
        {{ message.message.body.content }}
    </div>
</template>

<style scoped lang="scss"></style>