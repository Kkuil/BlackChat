<script setup lang="ts">
import { LoginDialogCompTypes } from "@/components/LoginDialog/type"
import QrCode from "qrcode.vue"
import { ref } from "vue"
import { popDownLoginDialog } from "@/utils/popLoginDialog"

defineProps<LoginDialogCompTypes.LoginDialogCompProps>()
const visible = ref<boolean>(true)

/**
 * 关闭弹窗
 */
const onClose = () => {
    visible.value = false
    setTimeout(popDownLoginDialog, 500)
}
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
                <div class="w-[350px] h-[350px] bg-[#f5f5f5] flex-center">
                    <QrCode
                        class="login-qrcode"
                        v-if="qrCodeUrl"
                        :value="qrCodeUrl"
                        :size="328"
                        :margin="5"
                    />
                </div>
            </div>
        </template>
        <template #footer>
            <div class="flex-center">
                使用「
                <span class="text-[#2aae67]">微信</span>
                」扫描二维码登录~~
            </div>
        </template>
    </el-dialog>
</template>

<style scoped lang="scss"></style>