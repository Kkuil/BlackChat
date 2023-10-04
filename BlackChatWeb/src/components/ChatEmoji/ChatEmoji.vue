<script setup lang="ts">
import EmojiList from "@/components/EmojiList/EmojiList.vue"
import { useMessageStore } from "@/stores/message"

const messageStore = useMessageStore()

const onSelect = (emoji: string) => {
    messageStore.addEmoji(emoji)
}
</script>

<template>
    <div class="chat-emoji">
        <div class="data w-full h-[300px] overflow-y-scroll">
            <EmojiList @select="onSelect" />
        </div>
    </div>
</template>

<style scoped lang="scss"></style>