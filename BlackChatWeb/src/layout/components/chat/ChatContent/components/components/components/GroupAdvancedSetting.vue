<script setup lang="ts">
import { GroupRoleEnum } from "@/enums/GroupRoleEnum"
import { MAX_ADMIN_COUNT } from "@/constant/group"
import { Avatar, RemoveFilled } from "@element-plus/icons-vue"
import AddAdminDialog from "@/components/AddAdminDialog/AddAdminDialog.vue"
import { useSessionStore } from "@/stores/session"
import { ref } from "vue"
import { ElMessage } from "element-plus"

const sessionStore = useSessionStore()

const isShowSelectAddAdminDialog = ref<boolean>(false)

const delAdminHandler = (uid: number) => {
    const isDel = sessionStore.delAdminHandler([uid] as number[])
    if (isDel) {
        ElMessage.success("删除成功")
    }
}
</script>

<template>
    <div
        class="w-full rounded-[10px] border-[1px] flex items-center justify-between p-[10px] my-[5px]"
        v-if="GroupRoleEnum.MASTER === sessionStore.currentRoleId"
    >
        <h3 class="text-[12px]">
            设置管理员（{{ sessionStore.countAdmin }}/{{ MAX_ADMIN_COUNT }}）
        </h3>
        <div class="flex items-center">
            <el-button
                type="primary"
                size="small"
                :disabled="sessionStore.countAdmin >= 3"
                @click="isShowSelectAddAdminDialog = true"
                class="mr-[5px]"
            >
                设置管理员
            </el-button>
            <ul class="admin-list flex">
                <li
                    v-for="item in sessionStore.listAdmin"
                    :key="item.uid"
                    class="flex-center mx-[1px]"
                >
                    <el-popconfirm
                        title="确认撤销管理权限吗？"
                        confirm-button-text="确认"
                        cancel-button-text="取消"
                        :icon="RemoveFilled"
                        icon-color="#626AEF"
                        @confirm="delAdminHandler(item.uid as number)"
                    >
                        <template #reference>
                            <el-badge value="-" class="cursor-pointer">
                                <el-avatar :size="25" :src="item.avatar">
                                    <el-icon :size="25">
                                        <Avatar />
                                    </el-icon>
                                </el-avatar>
                            </el-badge>
                        </template>
                    </el-popconfirm>
                </li>
            </ul>
        </div>
        <el-dialog
            v-model="isShowSelectAddAdminDialog"
            width="auto"
            align-center
            class="rounded-[15px] shadow-md bg-third"
        >
            <AddAdminDialog />
        </el-dialog>
    </div>
</template>

<style lang="scss">
.admin-list {
    .el-badge {
        display: flex;

        .el-badge__content {
            width: 15px;
            height: 15px;
        }
    }
}
</style>