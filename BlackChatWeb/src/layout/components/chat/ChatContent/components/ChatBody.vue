<script setup lang="ts">
import ChatMessageItemStrategy from "@/components/ChatMessageItem/ChatMessageItemStrategy.vue"
import { onMounted, ref } from "vue"
import { ChatMessageItemTypes } from "@/components/ChatMessageItem/type"

const configInfo: ChatMessageItemTypes.ChatMessageItemPropsType = {
    message: "已经到达顶部"
}

const bodyRef = ref<HTMLElement | null>()

onMounted(() => {
    initReachBottom()
})

const initReachBottom = () => {
    if ("scrollTop" in bodyRef.value) {
        bodyRef.value.scrollTop = bodyRef.value?.scrollHeight
    }
}
</script>

<template>
    <div class="h-full bg-secondary" ref="bodyRef">
        <ChatMessageItemStrategy :config-info="configInfo" type="system" />
    </div>
</template>

<style scoped lang="scss"></style>