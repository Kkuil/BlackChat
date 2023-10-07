<script setup lang="ts">
import { Avatar } from "@element-plus/icons-vue"
import { useUserStore } from "@/stores/user"
import { popUpLoginDialog } from "@/utils/popDialog/popLoginDialog"
import { popUpUserInfoDialog } from "@/utils/popDialog/popUserInfoDialog"

const userStore = useUserStore()

/**
 * 查看用户信息
 */
const checkUserInfo = async () => {
    // 判断用户是否登录
    if (userStore.isTempUser || !userStore.isLogin) {
        popUpLoginDialog()
    } else {
        popUpUserInfoDialog()
    }
}
</script>
<template>
    <div class="cursor-pointer flex-center flex-col" @click="checkUserInfo">
        <el-avatar :size="50" :src="userStore.userInfo.avatar">
            <el-icon :size="25">
                <Avatar />
            </el-icon>
        </el-avatar>
        <span class="mt-[7px] text-[#fff] text-[13px] hidden sm:inline-block">
            {{ userStore.userInfo.name ?? "未登录" }}
        </span>
    </div>
</template>

<style scoped></style>