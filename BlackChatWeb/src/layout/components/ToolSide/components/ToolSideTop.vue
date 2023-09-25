<script setup lang="ts">
import ToolSideAvatar from "@/layout/components/ToolSide/components/ToolSideAvatar.vue"
import MessageContact from "@/layout/components/ToolSide/components/MessageContact.vue"
</script>

<template>
    <div
        class="w-full h-full flex flex-row sm:flex-col items-center text-[#fff]"
    >
        <ToolSideAvatar avatar="123" name="Kkuil" />
        <MessageContact />
    </div>
</template>

<style scoped lang="scss">
.selected {
    background: #303442;
}
</style>