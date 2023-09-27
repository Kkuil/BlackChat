<script setup lang="ts">
import { ref } from "vue"
import { popDownUserInfoDialog } from "@/utils/popDialog/popUserInfoDialog"
import { useUserStore } from "@/stores/user"

const badges = ref<string[]>([
    "https://cdn-icons-png.flaticon.com/128/1533/1533913.png",
    "https://cdn-icons-png.flaticon.com/512/6198/6198527.png",
    "https://cdn-icons-png.flaticon.com/512/10232/10232583.png",
    "https://cdn-icons-png.flaticon.com/128/2909/2909937.png",
    "https://minio.mallchat.cn/mallchat/%E8%B4%A1%E7%8C%AE%E8%80%85.png"
])

const userStore = useUserStore()

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
                <li class="badge-item" v-for="item in badges" :key="item">
                    <img class="badge-item-icon" :src="item" alt="badge" />
                    <div class="badge-item-mask">
                        <i
                            class="el-icon badge-item-info el-tooltip__trigger el-tooltip__trigger"
                        >
                            <svg
                                viewBox="0 0 1024 1024"
                                width="1.2em"
                                height="1.2em"
                            >
                                <path
                                    fill="currentColor"
                                    d="M512 64a448 448 0 1 1 0 896.064A448 448 0 0 1 512 64zm67.2 275.072c33.28 0 60.288-23.104 60.288-57.344s-27.072-57.344-60.288-57.344c-33.28 0-60.16 23.104-60.16 57.344s26.88 57.344 60.16 57.344zM590.912 699.2c0-6.848 2.368-24.64 1.024-34.752l-52.608 60.544c-10.88 11.456-24.512 19.392-30.912 17.28a12.992 12.992 0 0 1-8.256-14.72l87.68-276.992c7.168-35.136-12.544-67.2-54.336-71.296c-44.096 0-108.992 44.736-148.48 101.504c0 6.784-1.28 23.68.064 33.792l52.544-60.608c10.88-11.328 23.552-19.328 29.952-17.152a12.8 12.8 0 0 1 7.808 16.128L388.48 728.576c-10.048 32.256 8.96 63.872 55.04 71.04c67.84 0 107.904-43.648 147.456-100.416z"
                                ></path>
                            </svg>
                        </i>
                    </div>
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