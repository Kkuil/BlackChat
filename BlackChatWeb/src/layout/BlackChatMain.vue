<script setup lang="ts">
import ToolSide from "@/layout/components/ToolSide/ToolSide.vue"
import ChatList from "@/layout/components/ChatList/ChatList.vue"
import ChatContent from "@/layout/components/ChatContent/ChatContent.vue"
import OnlineList from "@/layout/components/OnlineList/OnlineList.vue"
</script>

<template>
    <div
        class="w-[75%] h-[600px] bg-primary rounded-[15px] flex overflow-hidden p-[15px]"
    >
        <ToolSide class="flex-[6%]" />
        <ChatList class="flex-[29%]" />
        <ChatContent class="flex-[50%]" />
        <OnlineList class="flex-[15%]" />
    </div>
</template>