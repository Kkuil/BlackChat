<script setup lang="ts">
import { GroupRoleEnum } from "@/enums/GroupRoleEnum"
import { useSessionStore } from "@/stores/session"
import { MAX_ADMIN_COUNT } from "@/constant/group"
import AddAdminDialog from "@/components/AddAdminDialog/AddAdminDialog.vue"
import { ref } from "vue"

const sessionStore = useSessionStore()

const isShowSelectAdminDialog = ref<boolean>(false)
</script>

<template>
    <div>
        <el-divider content-position="left" class="my-[15px]">高级</el-divider>
        <div
            class="w-full h-[40px] rounded-[10px] border-[1px] flex items-center justify-between px-[10px]"
            v-if="GroupRoleEnum.MASTER === sessionStore.currentRoleId"
        >
            <h3 class="text-[12px]">
                设置管理员（{{ sessionStore.countAdmin }}/{{
                    MAX_ADMIN_COUNT
                }}）
            </h3>
            <el-button
                type="primary"
                size="small"
                :disabled="sessionStore.countAdmin >= 3"
                @click="isShowSelectAdminDialog = true"
            >
                设置管理员
            </el-button>
            <el-dialog
                v-model="isShowSelectAdminDialog"
                width="auto"
                align-center
                class="rounded-[15px] shadow-md"
            >
                <AddAdminDialog />
            </el-dialog>
        </div>
    </div>
</template>

<style lang="scss">
.el-dialog {
    background: #424656;
}
</style>