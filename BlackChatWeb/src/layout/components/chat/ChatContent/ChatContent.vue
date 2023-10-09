<script setup lang="ts">
import ChatInput from "@/layout/components/chat/ChatContent/components/ChatInput.vue"
import ChatBody from "@/layout/components/chat/ChatContent/components/ChatBody.vue"
import { useSessionStore } from "@/stores/session"
import { ElMessage, ElMessageBox } from "element-plus"

const sessionStore = useSessionStore()

/**
 * 退出群聊
 */
const exitGroup = () => {
    ElMessageBox.confirm(
        "是否退出该群聊？退出后，聊天相关记录也将会被同步删除",
        "退出群聊",
        {
            confirmButtonText: "确定",
            cancelButtonText: "取消"
        }
    )
        .then(() => {
            sessionStore.exitGroupRoom()
        })
        .catch(() => {
            ElMessage.error("退出群聊失败")
        })
}

/**
 * 删除好友
 */
const delFriend = () => {
    ElMessageBox.confirm(
        "是否删除该好友？删除后，聊天相关记录也将会被同步删除",
        "删除好友",
        {
            confirmButtonText: "确定",
            cancelButtonText: "取消"
        }
    )
        .then(() => {})
        .catch(() => {
            ElMessage.error("删除好友失败")
        })
}
</script>

<template>
    <div
        class="w-full h-full flex flex-col bg-secondary rounded-[10px] overflow-hidden"
    >
        <div
            class="w-full h-[8%] bg-third flex items-center justify-between px-[10px] text-[#f5f5f5] font-extrabold"
        >
            <h1>{{ sessionStore.getSessionInfo.name }}</h1>
            <el-button
                type="danger"
                size="small"
                v-if="
                    sessionStore.isGroup &&
                    sessionStore.getSessionInfo.hotFlag !== 1
                "
                @click="exitGroup"
            >
                退出群聊
            </el-button>
            <el-button
                type="danger"
                size="small"
                v-else-if="!sessionStore.isGroup"
                @click="delFriend"
            >
                删除好友
            </el-button>
        </div>
        <div class="px-[15px] py-[10px] h-[92%] flex flex-col">
            <ChatBody class="flex-1 overflow-y-scroll mb-[10px]" />
            <ChatInput />
        </div>
    </div>
</template>