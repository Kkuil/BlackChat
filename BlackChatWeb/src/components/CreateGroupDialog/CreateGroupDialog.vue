<script setup lang="ts">
import { ref } from "vue"
import { useFriendStore } from "@/stores/friend"
import { createGroup } from "@/api/user"
import { ElMessage } from "element-plus"

const friendStore = useFriendStore()

// 创建群聊搜索框
const selectedFriends = ref<number[]>([])
// 创建群聊申请
const applyComment = ref<string>("")

/**
 * 创建群聊操作
 */
const createGroupHandler = async () => {
    if (applyComment.value.trim().length <= 0) {
        return ElMessage.error("申请备注不能为空")
    }
    const result = await createGroup({
        uidList: selectedFriends.value,
        msg: applyComment.value
    })
    if (result.data) {
        ElMessage.success(result.message)
        reset()
    }
}

const reset = () => {
    applyComment.value = ""
    selectedFriends.value = []
}
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
            title="申请备注"
            placement="right"
            :width="200"
            trigger="click"
        >
            <template #reference>
                <el-button type="primary">创建</el-button>
            </template>
            <template #default>
                <el-input
                    v-model="applyComment"
                    placeholder="请输入申请备注"
                    :max="200"
                    size="small"
                    class="text-[#000]"
                />
                <div class="operation mt-[5px]">
                    <el-button
                        type="primary"
                        size="small"
                        @click="createGroupHandler"
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