<script setup lang="ts">
import ToolSideAvatar from "@/layout/components/ToolSide/components/ToolSideAvatar.vue"
import MessageContact from "@/layout/components/ToolSide/components/MessageContact.vue"
import { useUserStore } from "@/stores/user"
import { popUpLoginDialog } from "@/utils/popLoginDialog"

const userStore = useUserStore()

/**
 * 查看用户信息
 */
const checkUserInfo = () => {
    popUpLoginDialog({ qrCodeUrl: "123" })
}
</script>

<template>
    <div
        class="w-full h-full flex flex-row sm:flex-col items-center text-[#fff]"
    >
        <ToolSideAvatar avatar="123" name="Kkuil" @click="checkUserInfo" />
        <MessageContact />
    </div>
</template>

<style scoped lang="scss">
.selected {
    background: #303442;
}
</style>