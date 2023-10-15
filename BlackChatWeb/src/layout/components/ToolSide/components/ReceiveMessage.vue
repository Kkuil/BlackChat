<script setup lang="ts">
import { ref, watch } from "vue"
import { listReceiveMessage } from "@/api/list"
import { ReadStatusEnum } from "@/enums/ReadStatusEnum"
import MailMessageItem from "@/layout/components/ToolSide/components/components/MailMessageItem.vue"
import type { ToolSide } from "@/layout/components/ToolSide/components/type.d.ts"
import { updateByUidList } from "@/utils/userCache"
import { useUserStore } from "@/stores/user"
import LimitPage = GlobalTypes.LimitPage

const userStore = useUserStore()

/**
 * 是否展示收件箱
 */
const isShowReceiveDrawer = ref<boolean>(false)

const pageInfo = ref<LimitPage<ReadStatusEnum>>({
    current: 1,
    pageSize: 5,
    data: ReadStatusEnum.UNREAD
})

const messageInfo = ref<{
    messages: ToolSide.ReceiveMessageResp[]
    totalCount: number
}>({
    messages: [],
    totalCount: 0
})

const tabs = ref<
    {
        label: string
        type: ReadStatusEnum
    }[]
>([
    {
        label: "未读",
        type: 1
    },
    {
        label: "已读",
        type: 2
    }
])

const listMessage = async () => {
    const result = await listReceiveMessage(pageInfo.value)
    if (result.data) {
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
        messageInfo.value.messages = result.data.data.map((item) => {
            item.avatar = userStore.userInfoCache[item.uid + ""]?.avatar ?? ""
            item.name = userStore.userInfoCache[item.uid + ""]?.name ?? ""
            return item
        })
        messageInfo.value.messages = result.data.data
        messageInfo.value.totalCount = result.data.total
    }
}

const switchTab = async (status: ReadStatusEnum) => {
    pageInfo.value = {
        current: 1,
        pageSize: 5,
        data: status
    }
    await listMessage()
}

const switchPage = (current) => {
    pageInfo.value.current = current
}

watch(
    () => pageInfo.value,
    async () => {
        await listMessage()
    },
    {
        immediate: true,
        deep: true
    }
)
</script>

<template>
    <div
        class="item flex-center flex-col cursor-pointer"
        title="点击查看收件箱"
        @click="isShowReceiveDrawer = true"
    >
        <i
            class="iconfont icon-email sm:text-[35px] text-[#fff] text-[25px]"
        ></i>
        <span class="desc text-[#fff] text-[12px] hidden sm:inline-block">
            收件箱
        </span>
    </div>
    <el-drawer
        v-model="isShowReceiveDrawer"
        direction="ltr"
        custom-class="w-full md:w-1/2 lg:w-[30%]"
    >
        <template #header>
            <h4 class="text-[#f5f5f5]">收件箱</h4>
        </template>
        <template #default>
            <el-tabs
                v-model="pageInfo.data"
                class="demo-tabs"
                @tab-change="switchTab"
            >
                <el-tab-pane
                    v-for="tab in tabs"
                    :key="tab.type"
                    :label="tab.label"
                    :name="tab.type"
                >
                    <div v-if="messageInfo.messages.length">
                        <MailMessageItem
                            v-for="message in messageInfo.messages"
                            :key="message.id"
                            :message="message"
                            :type="pageInfo.data"
                            class="mt-[10px]"
                        />
                    </div>
                    <el-empty
                        v-else
                        description="暂无消息"
                        class="w-full h-full"
                    >
                        <template #image>
                            <i
                                class="iconfont icon-empty text-[80px] text-[#ccc]"
                            ></i>
                        </template>
                    </el-empty>
                </el-tab-pane>
            </el-tabs>
            <el-pagination
                background
                small
                hide-on-single-page
                layout="prev, pager, next"
                :page-size="pageInfo.pageSize"
                :total="messageInfo.totalCount"
                class="w-full flex-center mt-[10px]"
                @currentChange="switchPage"
            />
        </template>
    </el-drawer>
</template>

<style scoped lang="scss"></style>