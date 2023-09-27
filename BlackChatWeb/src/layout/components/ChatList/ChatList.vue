<script setup lang="ts">
import ChatItem from "@/layout/components/ChatList/components/ChatItem.vue"
</script>

<template>
    <div class="chat-list overflow-y-scroll pr-[10px]">
        <ChatItem
            v-for="index in 20"
            :key="index"
            avatar="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
            name="Kkuil"
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