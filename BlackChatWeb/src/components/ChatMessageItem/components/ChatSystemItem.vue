<script setup lang="ts">
import { ChatMessageItemTypes } from "@/components/ChatMessageItem/type"

defineProps<ChatMessageItemTypes.ChatSystemItemPropsType>()
</script>

<template>
    <div class="system flex-center text-[#777] text-sm">{{ message }}</div>
</template>

<style scoped lang="scss"></style>