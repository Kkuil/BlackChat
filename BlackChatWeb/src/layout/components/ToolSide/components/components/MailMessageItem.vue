<script setup lang="ts">
import type { ToolSide } from "@/layout/components/ToolSide/components/type.d.ts"
import { Avatar } from "@element-plus/icons-vue"
import { MailOperationStatusEnum } from "@/enums/MailOperationStatusEnum"
import { mailOperation } from "@/api/mail"
import { ElMessage } from "element-plus"
import { ReadStatusEnum } from "@/enums/ReadStatusEnum"
import { useUserStore } from "@/stores/user"

const props = defineProps<{
    message: ToolSide.ReceiveMessageResp
    type: ReadStatusEnum
}>()

const userStore = useUserStore()

const operation = async (status: MailOperationStatusEnum) => {
    const result = await mailOperation({
        id: props.message.id,
        status
    })
    if (result.data) {
        ElMessage.success("操作成功")
    }
}
</script>

<template>
    <div
        class="item relative rounded-[10px] bg-[#fff] hover:bg-opacity-95 transition-[background] cursor-pointer h-[60px] flex items-center px-[15px]"
    >
        <el-avatar
            :src="userStore.getBaseInfoInCache(props.message.uid).avatar"
            :alt="userStore.getBaseInfoInCache(props.message.uid).name"
            class="h-[45px] w-[45px] rounded-full"
        >
            <el-icon :size="25">
                <Avatar />
            </el-icon>
        </el-avatar>
        <div
            class="info w-[80%] h-[75%] flex items-center justify-between ml-[10px]"
        >
            <div class="left w-[80%] h-full flex flex-col justify-between">
                <h2 class="text-[14px]">{{ message.name }}</h2>
                <h4
                    class="text-[10px] text-[#777] whitespace-nowrap w-full overflow-hidden overflow-ellipsis"
                >
                    备注:{{ message.msg }}
                </h4>
            </div>
            <div
                class="w-full flex justify-end overflow-hidden overflow-ellipsis right text-[12px] whitespace-nowrap"
            >
                {{ message.content }}
            </div>
            <div
                class="mask hidden absolute top-0 left-0 w-full h-full bg-third bg-opacity-25"
                v-if="type === ReadStatusEnum.UNREAD"
            >
                <el-button
                    type="primary"
                    size="small"
                    @click="operation(MailOperationStatusEnum.AGREE)"
                    >同意
                </el-button>
                <el-button
                    type="danger"
                    size="small"
                    @click="operation(MailOperationStatusEnum.REFUSE)"
                    >不同意
                </el-button>
            </div>
        </div>
    </div>
</template>

<style scoped lang="scss">
.item {
    &:hover {
        .mask {
            display: flex !important;
            justify-content: center;
            align-items: center;
        }
    }
}
</style>