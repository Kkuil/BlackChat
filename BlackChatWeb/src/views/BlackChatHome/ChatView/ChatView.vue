<script setup lang="ts">
import ChatList from "@/layout/components/chat/ChatList/ChatList.vue"
import ChatContent from "@/layout/components/chat/ChatContent/ChatContent.vue"
import OnlineList from "@/layout/components/chat/OnlineList/OnlineList.vue"
import { useSessionStore } from "@/stores/session"
import { RoomTypeEnum } from "@/enums/RoomTypeEnum"

const sessionStore = useSessionStore()
</script>

<template>
    <ChatList class="hidden md:block md:flex-[25%] xl:flex-[25%]" />
    <ChatContent
        class="h-[92%] sm:flex sm:flex-[85%] md:flex-[71%] xl:flex-[50%]"
        :class="
            sessionStore.getSessionInfo.type != RoomTypeEnum.GROUP
                ? 'xl:flex-[69%]'
                : 'xl:flex-[50%]'
        "
    />
    <OnlineList
        class="hidden xl:flex xl:flex-[18%]"
        v-if="sessionStore.getSessionInfo.type == RoomTypeEnum.GROUP"
    />
</template>

<style scoped lang="scss"></style>