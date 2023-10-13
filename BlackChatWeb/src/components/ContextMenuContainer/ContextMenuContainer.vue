<script setup lang="ts">
import {
    ContextMenu,
    ContextMenuItem,
    MenuOptions
} from "@imengyu/vue3-context-menu"
import { useUserStore } from "@/stores/user"
import { useMessageStore } from "@/stores/message"
import type { ChatMessageResp } from "@/layout/components/chat/ChatContent/ChatMessageResp.d.ts"
import { MessageTypeEnum } from "@/enums/MessageTypeEnum"
import { ref } from "vue"
import { addFriend } from "@/api/user"
import { ElMessage } from "element-plus"

const props = defineProps<{
    // 消息体
    message: ChatMessageResp.ChatMessageBaseResp<any, any>
    // 菜单设置-其它的参数透传
    options?: MenuOptions
    // 菜单项
    items: string[]
}>()

const userStore = useUserStore()
const messageStore = useMessageStore()
const isCurrentUser = props?.message?.fromUser?.uid == userStore.userInfo.uid

const applyComment = ref<string>("")

/**
 * 增加艾特
 */
const onAite = () => {
    messageStore.addAite({
        uid: Number(props.message.fromUser.uid),
        name: props.message.fromUser.name
    })
}

// 添加朋友操作
const addFriendHandler = async () => {
    if (applyComment.value.trim().length <= 0) {
        return ElMessage.error("申请备注不能为空")
    }
    const result = await addFriend({
        repliedId: props.message.fromUser.uid as number,
        msg: applyComment.value
    })
    if (result.data) {
        ElMessage.success(result.message)
        applyComment.value = ""
    }
}

const onRecall = () => {}
const onReply = () => {
    messageStore.addReply({
        id: props.message.message.id,
        name: props.message.fromUser.name,
        content: getMessageShowInReply(props.message.message)
    })
}

const showInReplyMap = {
    [MessageTypeEnum.TEXT]: null,
    [MessageTypeEnum.FILE]: "[文件]",
    [MessageTypeEnum.IMAGE]: "[图片]",
    [MessageTypeEnum.SOUND]: "[语音]",
    [MessageTypeEnum.VIDEO]: "[视频]"
}

const getMessageShowInReply = (message: ChatMessageResp.Message<any, any>) => {
    return showInReplyMap[message.type] ?? message.body.content
}
</script>

<template>
    <ContextMenu
        :options="{
            theme: 'dark',
            x: 0,
            y: 0,
            ...props.options
        }"
    >
        <ContextMenuItem
            v-if="
                userStore.isLogin &&
                !isCurrentUser &&
                items.indexOf('aite') >= 0
            "
            label="艾特他"
            @click="onAite"
        >
            <template #icon>
                <i class="iconfont icon-aite"></i>
            </template>
        </ContextMenuItem>
        <el-popover
            title="申请备注"
            placement="right"
            :width="200"
            trigger="click"
        >
            <template #reference>
                <ContextMenuItem
                    v-if="
                        userStore.isLogin &&
                        !isCurrentUser &&
                        items.indexOf('add-friend') >= 0
                    "
                    label="加好友"
                >
                    <template #icon>
                        <i class="iconfont icon-plus"></i>
                    </template>
                </ContextMenuItem>
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
                        @click="addFriendHandler"
                    >
                        确定
                    </el-button>
                </div>
            </template>
        </el-popover>
        <ContextMenuItem
            v-if="isCurrentUser && items.indexOf('recall') >= 0"
            label="撤回消息"
            @click="onRecall"
        >
            <template #icon>
                <i class="iconfont icon-recall"></i>
            </template>
        </ContextMenuItem>
        <ContextMenuItem
            v-if="userStore.isLogin && items.indexOf('reply') >= 0"
            label="回复消息"
            @click="onReply"
        >
            <template #icon>
                <i class="iconfont icon-reply"></i>
            </template>
        </ContextMenuItem>
    </ContextMenu>
</template>

<style scoped lang="scss"></style>