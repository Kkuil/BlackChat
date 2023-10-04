<script setup lang="ts">
import {
    ContextMenuItem,
    ContextMenuSeparator,
    MenuOptions
} from "@imengyu/vue3-context-menu"
import { MessageTypeEnum } from "@/enums/MessageTypeEnum"
import { useUserStore } from "@/stores/user"

const props = defineProps<{
    // 消息体
    message: ChatMessageResp.ChatMessageBaseResp<any, any>
    // 菜单设置-其它的参数透传
    options?: MenuOptions
}>()

const userStore = useUserStore()

const onRecall = () => {}
const onBlockUser = () => {}
</script>

<template>
    <ContextMenu
        :options="{
            theme: 'dark',
            x: 0,
            y: 0,
            ...props.options
        }"
        :message="message"
    >
        <ContextMenuItem
            v-if="
                message.message.type === MessageTypeEnum.TEXT ||
                message.message.type === MessageTypeEnum.IMAGE
            "
            label="复制"
        >
            <template #icon>
                <i class="iconfont icon-copy"></i>
            </template>
        </ContextMenuItem>
        <ContextMenuItem
            v-if="userStore.isLogin || userStore.isAdmin"
            label="撤回消息"
            @click="onRecall"
        >
            <template #icon>
                <i class="iconfont icon-recall"></i>
            </template>
        </ContextMenuItem>
        <ContextMenuSeparator />
        <ContextMenuItem
            v-if="userStore.isLogin"
            label="拉黑(管理)"
            @click="onBlockUser"
        >
            <template #icon>
                <i class="iconfont icon-recall"></i>
            </template>
        </ContextMenuItem>
    </ContextMenu>
</template>

<style scoped lang="scss"></style>