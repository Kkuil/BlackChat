<script setup lang="ts">
import { RouterView } from "vue-router"
import { useUserStore } from "@/stores/user"
import { USER_INFO_LOC_KEY } from "@/constant/userKeys"
import eventBus from "@/utils/eventBus"
import { WsEventEnum } from "@/enums/websocket/WsEventEnum"
import { onMounted } from "vue"
import { MessageResponseTypes } from "@/core/websocket/types/MessageResponseTypes"

const userStore = useUserStore()

onMounted(() => {
    initListeners()
})

/**
 * 初始化监听器
 */
const initListeners = () => {
    // 监听登录成功事件
    eventBus.on(
        WsEventEnum.LOGIN_SUCCESS,
        ({ message }: { message: MessageResponseTypes.TLoginSuccess }) => {
            // 保存用户信息
            userStore.loginSuccess(message)
        }
    )
}

userStore.$subscribe((mutation, state) => {
    // 保存用户信息
    localStorage.setItem(USER_INFO_LOC_KEY, JSON.stringify(state.userInfo))
})
</script>

<template>
    <RouterView v-slot="{ Component }">
        <template v-if="Component">
            <Transition mode="out-in">
                <KeepAlive>
                    <Suspense>
                        <!-- 主要内容 -->
                        <component :is="Component"></component>

                        <!-- 加载中状态 -->
                        <template #fallback> 正在加载...</template>
                    </Suspense>
                </KeepAlive>
            </Transition>
        </template>
    </RouterView>
</template>