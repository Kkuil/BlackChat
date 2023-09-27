<script setup lang="ts">
import { RouterView } from "vue-router"
import { useUserStore } from "@/stores/user"
import { USER_INFO_LOC_KEY } from "@/constant/userKeys"

const userStore = useUserStore()

userStore.$subscribe((mutation, state) => {
    // 保存用户信息
    localStorage.setItem(USER_INFO_LOC_KEY, JSON.stringify(state.userInfo))
})
</script>

<template>
    <RouterView />
</template>