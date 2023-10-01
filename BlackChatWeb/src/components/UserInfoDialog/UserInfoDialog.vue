<script setup lang="ts">
import { onMounted, ref } from "vue"
import { popDownUserInfoDialog } from "@/utils/popDialog/popUserInfoDialog"
import { useUserStore } from "@/stores/user"
import { useBadgeStore } from "@/stores/badge"

const badgePageInfo = ref<GlobalTypes.LimitPage<any>>({
    pageSize: 10,
    current: 1,
    data: null
})

const userStore = useUserStore()
const badgeStore = useBadgeStore()

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
                <h1 class="mt-[10px] text-2xl">
                    {{ userStore.userInfo.name }}
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