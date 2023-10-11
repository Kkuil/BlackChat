<script setup lang="ts">
import { reactive, ref, watch } from "vue"
import _ from "lodash"
import { addFriend, search } from "@/api/user"
import { updateByUidList } from "@/utils/userCache"
import { useUserStore } from "@/stores/user"
import { ElMessage } from "element-plus"
import LimitPage = GlobalTypes.LimitPage

export type TSearchResp = {
    uid: number
    name: string
    avatar: string
    isFriend: true
}

const userStore = useUserStore()

// 搜索参数
const searchParams = reactive<LimitPage<string>>({
    current: 1,
    pageSize: 5,
    data: ""
})
// 搜索关键词
const searchInput = ref<string>()
// 申请备注信息
const applyComment = ref<string>("")

// 搜索列表
const searchList = ref<TSearchResp[]>([])

// 添加朋友操作
const addFriendHandler = async (repliedId: number) => {
    if (applyComment.value.trim().length <= 0) {
        return ElMessage.error("申请备注不能为空")
    }
    const result = await addFriend({
        repliedId,
        msg: applyComment.value
    })
    if (result.data) {
        ElMessage.success(result.message)
        reset()
    }
}

const reset = () => {
    applyComment.value = ""
}

watch(
    () => searchInput.value,
    _.debounce(async (params: string) => {
        console.log(params)
        if (params.length) {
            const result = await search({
                ...searchParams,
                data: params
            })
            if (result.data && result.data.data.length) {
                const uidList: number[] = []
                // 收集未缓存的数据
                result.data.data.forEach((item) => {
                    if (!userStore.userInfoCache[item.uid + ""]) {
                        uidList.push(item.uid)
                    }
                })
                // 获取缓存数据
                if (uidList.length > 0) {
                    await updateByUidList(uidList)
                    // 将缓存加载到内存中来
                    userStore.refreshCache()
                }
                // 缓存映射
                searchList.value = result.data.data.map((item) => {
                    item.avatar =
                        userStore.userInfoCache[item.uid + ""]?.avatar ?? ""
                    item.name =
                        userStore.userInfoCache[item.uid + ""]?.name ?? ""
                    return item
                })
            }
        } else {
            searchList.value = []
        }
    }, 500)
)
</script>

<template>
    <div>
        <el-input
            v-model="searchInput"
            class="bg-primary h-[45px] text-[20px] text-[#cfd3dc]"
            clearable
        >
            <template #prefix>
                <i class="iconfont icon-search text-[20px]"></i>
            </template>
        </el-input>
        <div>
            <TransitionGroup>
                <div
                    class="search_list-item h-[60px] transition-[background] hover:bg-opacity-70 cursor-pointer flex items-center justify-between bg-primary px-[10px] my-[5px]"
                    v-for="item in searchList"
                    :key="item.uid"
                >
                    <div class="h-full flex items-center">
                        <img
                            :src="item.avatar"
                            :alt="item.name"
                            class="h-[80%] rounded-full mr-[10px]"
                        />
                        <h2 class="text-[#f5f5f5] text-[15px]">
                            {{ item.name }}
                        </h2>
                    </div>
                    <el-popover
                        title="申请备注"
                        placement="right"
                        :width="200"
                        trigger="click"
                    >
                        <template #reference>
                            <el-button
                                type="primary"
                                size="small"
                                :disabled="item.isFriend"
                            >
                                {{ !item.isFriend ? "加好友" : "已经是好友了" }}
                            </el-button>
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
                                    @click="addFriendHandler(item.uid)"
                                >
                                    确定
                                </el-button>
                            </div>
                        </template>
                    </el-popover>
                </div>
            </TransitionGroup>
        </div>
    </div>
</template>

<style lang="scss">
.el-input__wrapper {
    background: #2c2c2c;
}

.el-input__inner {
    color: #fff;
}

.el-dialog__header {
    display: none;
}

.el-popover {
    .el-popover__title {
        font-size: 13px !important;
        margin-bottom: 5px !important;
    }

    .el-input__wrapper {
        background: #fff;
    }

    .el-input__inner {
        color: #000;
    }
}

.v-enter-active,
.v-leave-active {
    transition: all 1s ease;
}

.v-enter-from,
.v-leave-to {
    opacity: 0;
    transform: translateX(20px);
}

.v-enter-to,
.v-leave-from {
    opacity: 1;
    transform: translateX(20px);
}
</style>