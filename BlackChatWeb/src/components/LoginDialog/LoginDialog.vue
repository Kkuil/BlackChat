<script setup lang="ts">
import { LoginDialogCompTypes } from "@/components/LoginDialog/type"
import { ref } from "vue"
import { popDownLoginDialog } from "@/utils/popLoginDialog"

const props = defineProps<LoginDialogCompTypes.LoginDialogCompProps>()
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
        title="BlackChat聊天室登录"
        width="30%"
        align-center
        @close="onClose"
    >
        <img :src="qrCodeUrl" alt="blackchat" title="blackchat" />
    </el-dialog>
</template>

<style scoped lang="scss"></style>