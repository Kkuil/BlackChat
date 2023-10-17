<script setup lang="ts">
import { GroupRoleEnum } from "@/enums/GroupRoleEnum"
import { ElMessageBox } from "element-plus"
import { useSessionStore } from "@/stores/session"

const sessionStore = useSessionStore()
const ROLE_MESSAGE_MAP = {
    [GroupRoleEnum.MASTER]:
        "你是群主，退出后，该房间的所有信息将被删除，确定删除吗？"
}

/**
 * 退出群聊
 */
const exitGroup = () => {
    ElMessageBox.confirm(
        `${
            ROLE_MESSAGE_MAP[sessionStore.currentRoleId] ??
            "是否退出该群聊？退出后，会话记录也将会被同步删除"
        }`,
        "退出群聊",
        {
            confirmButtonText: "确定",
            cancelButtonText: "取消"
        }
    ).then(() => {
        sessionStore.exitGroupRoom()
    })
}
</script>

<template>
    <div
        class="w-full h-[40px] rounded-[10px] border-[1px] flex items-center justify-between px-[10px]"
    >
        <h3 class="text-[12px]">退出群聊</h3>
        <el-button type="danger" size="small" @click="exitGroup">
            退出群聊
        </el-button>
    </div>
</template>

<style scoped lang="scss"></style>