<script setup lang="ts">
import ToolSide from "@/layout/components/ToolSide/ToolSide.vue"
import ChatList from "@/layout/components/ChatList/ChatList.vue"
import ChatContent from "@/layout/components/ChatContent/ChatContent.vue"
import OnlineList from "@/layout/components/OnlineList/OnlineList.vue"
</script>

<template>
    <div
        class="w-full sm:w-[75%] h-full sm:h-[600px] bg-primary rounded-[0px] sm:rounded-[15px] flex flex-col justify-between sm:flex-row overflow-hidden p-[15px]"
    >
        <ToolSide
            class="hidden md:flex md:flex-[10%] xl:flex-[7%] rounded-[10px] bg-secondary sm:bg-transparent"
        />
        <ChatList class="hidden xl:block xl:flex-[28%]" />
        <ChatContent
            class="flex-[1/2] sm:flex sm:flex-[76%] md:flex-[71%] xl:flex-[50%]"
        />
        <OnlineList
            class="hidden sm:flex sm:flex-[24%] md:flex-[19%] xl:flex-[15%]"
        />
    </div>
</template>