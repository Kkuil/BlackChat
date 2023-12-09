<script setup lang="ts">
import {
    ContextMenu,
    ContextMenuItem,
    MenuOptions
} from "@imengyu/vue3-context-menu"
import { useUserStore } from "@/stores/user"
import { useMessageStore } from "@/stores/message"
import type { ChatMessageResp } from "@/layout/components/chat/ChatContent/ChatMessageResp.d.ts"
import { ref } from "vue"
import { addFriend } from "@/api/user"
import { ElMessage } from "element-plus"
import { revoke } from "@/api/message"
import { useSessionStore } from "@/stores/session"
import { SHOW_IN_REPLY_MAP } from "@/constant/global"

const props = defineProps<{
    // 消息体
    message: ChatMessageResp.ChatMessageBaseResp<any, any>
    // 菜单设置-其它的参数透传
    options?: MenuOptions
    // 菜单项
    items: string[]
}>()

const $emit = defineEmits(["multi-select"])

const userStore = useUserStore()
const messageStore = useMessageStore()
const sessionStore = useSessionStore()
const isCurrentUser = props?.message?.fromUser?.uid == userStore.userInfo.uid

const applyComment = ref<string>("")

/**
 * 增加艾特
 */
const onAite = () => {
    messageStore.addAite({
        uid: Number(props.message.fromUser.uid),
        name: userStore.getBaseInfoInCache(props.message.fromUser.uid).name
    })
}

/**
 * 添加朋友操作
 */
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

/**
 * 撤回消息
 */
const onRevoke = async () => {
    console.log(props.message.message.id)
    const result = await revoke({
        msgId: props.message.message.id,
        roomId: sessionStore.sessionInfo.chattingId
    })
    if (result.data) {
        ElMessage.success("撤回成功")
    }
}

/**
 * 回复消息
 */
const onReply = () => {
    messageStore.addReply({
        id: props.message.message.id,
        name: userStore.getBaseInfoInCache(props.message.fromUser.uid).name,
        content: getMessageShowInReply(props.message.message)
    })
}

const getMessageShowInReply = (message: ChatMessageResp.Message<any, any>) => {
    return SHOW_IN_REPLY_MAP[message.type] ?? message.body.content
}

/**
 * 多选消息
 */
const onMultiSelect = () => {
    $emit("multiSelect", props.message)
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
            @click="onRevoke"
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
        <ContextMenuItem label="多选" @click="onMultiSelect">
            <template #icon>
                <i class="iconfont icon-multiple"></i>
            </template>
        </ContextMenuItem>
    </ContextMenu>
</template>

<style scoped lang="scss"></style>