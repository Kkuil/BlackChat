<script setup lang="ts">
import ChatList from "@/layout/components/chat/ChatList/ChatList.vue"
import ChatContent from "@/layout/components/chat/ChatContent/ChatContent.vue"
import OnlineList from "@/layout/components/chat/OnlineList/OnlineList.vue"
</script>

<template>
    <ChatList class="hidden xl:block xl:flex-[28%]" />
    <ChatContent
        class="flex-[1/2] sm:flex sm:flex-[76%] md:flex-[71%] xl:flex-[50%]"
    />
    <OnlineList
        class="hidden sm:flex sm:flex-[24%] md:flex-[19%] xl:flex-[15%]"
    />
</template>

<style scoped lang="scss"></style>