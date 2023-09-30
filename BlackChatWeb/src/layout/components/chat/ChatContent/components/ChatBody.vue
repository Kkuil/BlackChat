<script setup lang="ts">
import ChatMessageItemStrategy from "@/components/ChatMessageItem/ChatMessageItemStrategy.vue"
import { ChatMessageItemTypes } from "@/components/ChatMessageItem/type"
import { onMounted, ref } from "vue"

const configInfo: ChatMessageItemTypes.ChatMessageItemPropsType = {
    avatar: "https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJkQXoMolPPP0JVa8DF1kJ50nicQ1HJvYwXBoicBNVwlzlFNB23m0KCmd4AML7jE7icpwU7xCZJZ5pMA/132",
    username: "Kkuil",
    message: "123",
    sendTime: "2023/9/24 12:30:00"
}

const bodyRef = ref<HTMLElement | null>()

onMounted(() => {
    initReachBottom()
})

const initReachBottom = () => {
    bodyRef.value.scrollTop = bodyRef.value?.scrollHeight
}
</script>

<template>
    <div class="h-full bg-secondary" ref="bodyRef">
        <ChatMessageItemStrategy
            v-for="index in 30"
            :key="index"
            :type="index % 2 ? 'text' : 'system'"
            :config-info="configInfo"
        />
    </div>
</template>

<style scoped lang="scss"></style>