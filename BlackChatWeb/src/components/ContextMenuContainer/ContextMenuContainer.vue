<script setup lang="ts">
import {
    ContextMenu,
    ContextMenuItem,
    MenuOptions
} from "@imengyu/vue3-context-menu"
import { useUserStore } from "@/stores/user"
import { useMessageStore } from "@/stores/message"

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

const onAite = () => {
    console.log(props?.message?.fromUser?.uid)
}

const onAddFriend = () => {
    console.log(props?.message?.fromUser?.uid)
}

const onRecall = () => {}
const onReply = () => {
    messageStore.addReply({
        id: props?.message?.message?.id,
        name: props?.message?.fromUser?.name,
        content: props?.message?.message?.body?.content
    })
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
        <ContextMenuItem
            v-if="
                userStore.isLogin &&
                !isCurrentUser &&
                items.indexOf('add-friend') >= 0
            "
            label="加好友"
            @click="onAddFriend"
        >
            <template #icon>
                <i class="iconfont icon-plus"></i>
            </template>
        </ContextMenuItem>
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