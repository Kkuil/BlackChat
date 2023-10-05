<script setup lang="ts">
import QrCode from "qrcode.vue"
import { onMounted, onUnmounted, ref } from "vue"
import { popDownLoginDialog } from "@/utils/popDialog/popLoginDialog"
import eventBus from "@/utils/eventBus"
import { WsEventEnum } from "@/enums/websocket/WsEventEnum"
import { WorkerTypeEnum } from "@/core/websocket/domain/enum/WorkerTypeEnum"
import { LOGIN_MESSAGE } from "@/core/websocket/domain/preset/WsMessagePreset"
import Websocket from "@/core/websocket/Websocket"
import type { WebsocketTypes } from "@/core/websocket/types/type"
import { SuccessFilled } from "@element-plus/icons-vue"

/**
 * 弹框可见性
 */
const visible = ref<boolean>(true)

/**
 * 登录二维码
 */
const qrCodeWithLogin = ref<string>("")

/**
 * 是否已经扫描二维码
 */
const isScanned = ref<boolean>(false)

onMounted(() => {
    init()
})

/**
 * 初始化
 */
const init = () => {
    initListeners()
    // 获取二维码链接
    const workerMessage: WebsocketTypes.WorkerParamsType = {
        type: WorkerTypeEnum.MESSAGE,
        data: LOGIN_MESSAGE
    }
    // 向后端发起获取二维码的请求
    Websocket.send(workerMessage)
}

/**
 * 初始化监听器
 */
const initListeners = () => {
    // 监听获取到二维码的事件
    eventBus.on(WsEventEnum.GET_QR_CODE, (data: { url: string }) => {
        qrCodeWithLogin.value = data.url
    })

    // 监听登录成功事件
    eventBus.on(WsEventEnum.LOGIN_SUCCESS, () => {
        onClose()
    })
}

/**
 * 关闭弹窗
 */
const onClose = () => {
    visible.value = false
    setTimeout(popDownLoginDialog, 500)
}

/**
 * 刷新二维码
 */
const refreshQrCode = () => {
    // 发送刷新二维码事件
    eventBus.emit(WsEventEnum.REFRESH_QR_CODE)
}

onUnmounted(() => {
    // 取消监听获取二维码事件
    eventBus.off(WsEventEnum.GET_QR_CODE)
})
</script>

<template>
    <el-dialog
        v-model="visible"
        width="30%"
        align-center
        @close="onClose"
        class="rounded-[15px] shadow-md"
    >
        <template #header>
            <h1 class="flex-center text-xl">BlackChat聊天室登录</h1>
        </template>
        <template #default>
            <div class="w-full flex-center">
                <div
                    class="w-[350px] h-[350px] bg-[#f5f5f5] flex-center"
                    v-loading="!qrCodeWithLogin"
                >
                    <QrCode
                        class="login-qrcode"
                        v-if="qrCodeWithLogin"
                        :value="qrCodeWithLogin"
                        :size="328"
                        :margin="5"
                    />
                </div>
            </div>
        </template>
        <template #footer>
            <div v-show="isScanned" class="scanned flex-center text-[#2aae67]">
                <el-icon>
                    <SuccessFilled />
                </el-icon>
                <span class="ml-[5px]">扫码成功</span>
            </div>
            <div class="refresh flex-center">
                失效？<span
                    class="text-[#2aae67] cursor-pointer"
                    @click="refreshQrCode"
                >
                    点击此处刷新吧~
                </span>
            </div>
            <div class="scan flex-center">
                使用「
                <span class="text-[#2aae67]">微信</span>
                」扫描二维码登录~~
            </div>
        </template>
    </el-dialog>
</template>

<style scoped lang="scss"></style>