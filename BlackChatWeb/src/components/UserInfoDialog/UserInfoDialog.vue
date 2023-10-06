<script setup lang="ts">
import { onMounted, ref } from "vue"
import { popDownUserInfoDialog } from "@/utils/popDialog/popUserInfoDialog"
import { useUserStore } from "@/stores/user"
import { useBadgeStore } from "@/stores/badge"
import { updateUsername } from "@/api/user"
import { ElMessage } from "element-plus"
import { TOKEN_KEY_IN_LOC } from "@/constant/auth"
import { pushMessageToMainThread } from "@/core/websocket/Worker"
import { WorkerTypeEnum } from "@/core/websocket/domain/enum/WorkerTypeEnum"
import { WsRequestTypeEnum } from "@/core/websocket/domain/enum/WsRequestTypeEnum"

const userStore = useUserStore()
const badgeStore = useBadgeStore()

type TType = {
    isTypingName: boolean
    name: string
}

/**
 * 更改名字信息
 */
const type = ref<TType>({
    isTypingName: false,
    name: ""
})

/**
 * 徽章列表信息
 */
const badgePageInfo = ref<GlobalTypes.LimitPage<any>>({
    pageSize: 10,
    current: 1,
    data: null
})

onMounted(() => {
    getBadgeList()
})

/**
 *  弹窗是否可见
 */
const visible = ref<boolean>(true)

/**
 * 关闭弹窗
 */
const onClose = () => {
    visible.value = false
    setTimeout(popDownUserInfoDialog, 500)
}

/**
 * 获取徽章列表
 */
const getBadgeList = async () => {
    await badgeStore.updateBadges(badgePageInfo.value)
}

/**
 * 更改名字
 */
const updateName = async () => {
    const result = await updateUsername(type.value.name)
    if (result.data) {
        userStore.updateName(type.value.name)
        type.value.isTypingName = false
        ElMessage.success("更改成功")
    }
}

/**
 * 退出登录
 */
const logout = () => {
    // 1. 删除token
    const token = localStorage.getItem(TOKEN_KEY_IN_LOC)
    localStorage.removeItem(TOKEN_KEY_IN_LOC)
    // 2. 通知后端修改用户状态为离线
    const logoutMessage = {
        type: WorkerTypeEnum.MESSAGE,
        data: {
            type: WsRequestTypeEnum.LOGOUT,
            data: token
        }
    }
    pushMessageToMainThread(logoutMessage)
    // 3. 关闭弹框
    onClose()
    window.open()
}
</script>

<template>
    <el-dialog
        v-model="visible"
        width="45%"
        align-center
        @close="onClose"
        class="rounded-[15px] shadow-md"
    >
        <template #default>
            <div class="w-full h-[200px] flex-center flex-col">
                <el-avatar :src="userStore.userInfo.avatar" :size="120" />
                <h1 class="flex items-center mt-[10px] text-2xl">
                    <span v-show="!type.isTypingName">
                        {{ userStore.userInfo.name }}
                    </span>
                    <el-input
                        v-show="type.isTypingName"
                        v-model="type.name"
                        :placeholder="userStore.userInfo.name"
                    />
                    <i
                        class="flex-center iconfont icon-editor text-[25px] ml-[10px] cursor-pointer w-[30px] h-[30px] hover:bg-[#ccc] transition-[background-color] rounded-[5px]"
                        title="点击更名"
                        @click="type.isTypingName = true"
                        v-if="!type.isTypingName"
                    ></i>
                    <el-button
                        type="primary"
                        v-show="type.isTypingName"
                        class="ml-[10px]"
                        @click="updateName"
                    >
                        保存
                    </el-button>
                    <el-button type="primary" class="ml-[10px]" @click="logout">
                        退出登录
                    </el-button>
                </h1>
            </div>
            <ul class="badge-list">
                <li
                    class="badge-item cursor-pointer"
                    :class="item.obtain ? 'grayscale-0' : ''"
                    v-for="item in badgeStore.list"
                    :key="item"
                >
                    <el-tooltip
                        effect="dark"
                        :content="item.describe"
                        placement="top"
                    >
                        <img
                            class="badge-item-icon"
                            :src="item.img"
                            alt="badge"
                        />
                    </el-tooltip>
                </li>
            </ul>
        </template>
    </el-dialog>
</template>

<style scoped lang="scss">
.badge-list {
    width: 100%;
    display: flex;
    justify-content: space-between;

    .badge-item {
        width: 100px;
        height: 100px;
        filter: grayscale(1);
    }
}

.owned {
    filter: grayscale(0) !important;
}
</style>