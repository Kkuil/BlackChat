<script setup lang="ts">
import FriendInfo = Store.FriendInfo
import { Avatar } from "@element-plus/icons-vue"
import { useFriendStore } from "@/stores/friend"
import { ElMessageBox } from "element-plus"
import { useRouter } from "vue-router"
import { useSessionStore } from "@/stores/session"

const friendStore = useFriendStore()

const props = defineProps<{
    user: Partial<FriendInfo>
}>()

const $router = useRouter()

const sessionStore = useSessionStore()

/**
 * 删除好友
 */
const delFriend = () => {
    ElMessageBox.confirm(
        "确定删除该好友吗？删除后，相关聊天记录将无法恢复。",
        "删除好友",
        {
            confirmButtonText: "确定",
            cancelButtonText: "取消"
        }
    ).then(() => {
        friendStore.deleteFriend(props.user.uid as number)
    })
}

/**
 * 发消息
 */
const returnSendMsg = () => {
    $router.push({
        path: "/chat"
    })
    sessionStore.switchSession(props.user.roomId as string)
}
</script>

<template>
    <div class="contact-info w-full h-full px-[30px] bg-primary">
        <div class="flex user-info">
            <el-avatar :src="user.avatar" :size="85">
                <el-icon :size="60">
                    <Avatar />
                </el-icon>
            </el-avatar>
            <div class="h-full info ml-[10px] font-sans">
                <h1 class="text-[17px] text-[#f5f5f5] mb-[5px]">
                    用户名：{{ user.name }}
                </h1>
                <h2 class="text-[14px] text-[#666] mb-[5px]">
                    uid: {{ user.uid }}
                </h2>
                <h1 class="text-[14px] text-[#666] mb-[5px]">
                    地区：{{ user.place ?? "未知" }}
                </h1>
            </div>
            <div class="ml-auto flex justify-around flex-col operation">
                <el-button
                    size="small"
                    type="primary"
                    class="w-[100px]"
                    @click="returnSendMsg"
                >
                    发消息
                </el-button>
                <el-button
                    size="small"
                    type="danger"
                    class="w-[100px] m-0"
                    @click="delFriend"
                >
                    删除
                </el-button>
            </div>
        </div>
        <el-divider content-position="center" />
    </div>
</template>

<style scoped lang="scss"></style>