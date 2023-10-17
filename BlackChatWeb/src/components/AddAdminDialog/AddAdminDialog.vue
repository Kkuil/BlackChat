<script setup lang="ts">
import { ref } from "vue"
import { useSessionStore } from "@/stores/session"
import { ElMessage } from "element-plus"
import { addAdmin } from "@/api/group"

const sessionStore = useSessionStore()

// 创建成员搜索框
const selectedMembers = ref<number[]>([])

/**
 * 创建群聊操作
 */
const addAdminHandler = async () => {
    if (!selectedMembers.value.length) {
        return ElMessage.error("请选择要添加管理的成员")
    }
    const addAdminData = {
        groupId: sessionStore.getSessionInfo.roomId,
        uidList: selectedMembers.value
    }
    const result = await addAdmin(addAdminData)
    if (result.data) {
        ElMessage.success("添加成功")
        // 设置管理员
        sessionStore.setAdmins(selectedMembers.value)
        reset()
    }
}

const reset = () => {}
</script>

<template>
    <el-transfer
        :props="{
            key: 'uid',
            label: 'name',
            disabled: 'roleId'
        }"
        v-model="selectedMembers"
        :data="sessionStore.sessionInfo.memberList"
        :titles="['未选中', '已选中']"
    />
    <div class="flex-center mt-[10px]">
        <el-button type="primary" @click="addAdminHandler">添加</el-button>
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