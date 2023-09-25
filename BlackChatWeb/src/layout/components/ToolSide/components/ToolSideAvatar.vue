<script setup lang="ts">
import { Avatar } from "@element-plus/icons-vue"
import { useUserStore } from "@/stores/user"
import { popUpLoginDialog } from "@/utils/popLoginDialog"
import Websocket from "@/core/websocket/Websocket"
import { WebsocketTypes } from "@/core/websocket/types/type"
import { WsRequestTypeEnum } from "@/core/websocket/domain/enum/WsRequestTypeEnum"
import { WorkerTypeEnum } from "@/core/websocket/domain/enum/WorkerTypeEnum"
import eventBus from "@/utils/eventBus"
import { WsResponseTypeEnum } from "@/core/websocket/domain/enum/WsResponseTypeEnum"

const userStore = useUserStore()

/**
 * 查看用户信息
 */
const checkUserInfo = async () => {
    // 判断用户是否登录
    if (!userStore.userInfo.uid) {
        // 获取二维码链接
        const workerMessage: WebsocketTypes.WorkerParamsType = {
            type: WorkerTypeEnum.MESSAGE,
            data: JSON.stringify({
                type: WsRequestTypeEnum.LOGIN,
                data: ""
            })
        }

        Websocket.send(workerMessage)
        eventBus.on(WsResponseTypeEnum.LOGIN_URL, (data: { url: string }) => {
            popUpLoginDialog({ qrCodeUrl: data.url })
        })
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
        <span class="mt-[7px] text-[#fff] text-[14px] hidden sm:inline-block">
            {{ userStore.userInfo.name ?? "Kkuil" }}
        </span>
    </div>
</template>

<style scoped></style>