<script setup lang="ts">
import { onBeforeMount, ref } from "vue"
import { useFriendStore } from "@/stores/friend"
import { useUserStore } from "@/stores/user"
import { inviteGroup } from "@/api/user"
import { useSessionStore } from "@/stores/session"
import { ElMessage } from "element-plus"

export type TInviteGroup = {
    groupId: number
    msg: string
    uidList: number[]
}

const friendStore = useFriendStore()
const sessionStore = useSessionStore()
const userStore = useUserStore()

// 创建群聊搜索框
const selectedFriends = ref<number[]>([])
const msg = ref<string>("")

/**
 * 创建群聊操作
 */
const inviteGroupHandler = async () => {
    if (!msg.value.trim().length) {
        return ElMessage.error("邀请备注不能为空")
    }
    if (!selectedFriends.value.length) {
        return ElMessage.error("请选择好友")
    }
    const inviteGroupData: TInviteGroup = {
        groupId: sessionStore.getSessionInfo.roomId,
        msg: msg.value,
        uidList: selectedFriends.value
    }
    const result = await inviteGroup(inviteGroupData)
    if (result.data) {
        ElMessage.success("邀请成功")
        reset()
    }
}

const reset = () => {
    msg.value = ""
    selectedFriends.value = []
}

onBeforeMount(() => {
    friendStore.getFriends()
})
</script>

<template>
    <el-transfer
        :props="{
            key: 'uid',
            label: 'name'
        }"
        v-model="selectedFriends"
        :data="friendStore.friends"
        :titles="['未选中', '已选中']"
    />
    <div class="flex-center mt-[5px]">
        <el-popover
            title="邀请备注"
            placement="right"
            :width="200"
            trigger="click"
        >
            <template #reference>
                <el-button type="primary">邀请</el-button>
            </template>
            <template #default>
                <el-input
                    v-model="msg"
                    placeholder="请输入邀请备注"
                    :max="200"
                    size="small"
                    class="text-[#000]"
                />
                <div class="operation mt-[5px]">
                    <el-button
                        type="primary"
                        size="small"
                        @click="inviteGroupHandler"
                    >
                        确定
                    </el-button>
                </div>
            </template>
        </el-popover>
    </div>
</template>

<style lang="scss">
.el-transfer-panel {
    background-color: #13171e;

    .el-transfer-panel__header {
        background-color: #13171e;
    }
}
</style>