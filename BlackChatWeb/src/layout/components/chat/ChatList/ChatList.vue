<script setup lang="ts">
import ChatItem from "@/layout/components/chat/ChatList/components/ChatItem.vue"
</script>

<template>
    <div class="chat-list overflow-y-scroll pr-[10px]">
        <ChatItem
            avatar="https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJkQXoMolPPP0JVa8DF1kJ50nicQ1HJvYwXBoicBNVwlzlFNB23m0KCmd4AML7jE7icpwU7xCZJZ5pMA/132"
            name="Blackchat全员群聊"
            message="Kkuil: HelloWorlddawdwaddawddawdwadwadaw"
            class="item mb-[10px]"
        />
    </div>
</template>

<style scoped lang="scss">
.chat-list {
    .item:last-child {
        margin-bottom: 0 !important;
    }
}
</style>