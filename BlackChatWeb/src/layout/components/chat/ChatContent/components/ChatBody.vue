<script setup lang="ts">
import ChatMessageItemStrategy from "@/components/ChatMessageItem/ChatMessageItemStrategy.vue"
import { MessageTypeEnum } from "@/enums/MessageTypeEnum"
import { useMessageStore } from "@/stores/message"
import { onBeforeUnmount, onMounted, ref, watch } from "vue"
import type { ChatMessageResp } from "@/layout/components/chat/ChatContent/ChatMessageResp.d.ts"
import { WsEventEnum } from "@/enums/websocket/WsEventEnum"
import eventBus from "@/utils/eventBus"
import { useSessionStore } from "@/stores/session"
import { useUserStore } from "@/stores/user"
import _ from "lodash"
import SystemMessageBody = ChatMessageResp.SystemMessageBody

const messageStore = useMessageStore()
const sessionStore = useSessionStore()
const userStore = useUserStore()
const bodyRef = ref<HTMLDivElement>()
const isShowNewMessage = ref<boolean>(false)
const lastMessage: ChatMessageResp.ChatMessageBaseResp<SystemMessageBody, any> =
    {
        fromUser: {
            uid: "10003"
        },
        message: {
            id: 3170000,
            sendTime: "20210317",
            type: MessageTypeEnum.SYSTEM,
            body: {
                content: "已到达顶部"
            }
        }
    }

const scrollBottom = () => {
    setTimeout(() => {
        if ("scrollTop" in bodyRef.value) {
            console.log("top: ", bodyRef.value?.scrollTop)
            console.log("height: ", bodyRef.value?.scrollHeight)
            bodyRef.value.scrollTop = bodyRef.value?.scrollHeight
        }
    }, 100)
}

watch(
    () => messageStore.messageList,
    () => {
        scrollBottom()
    },
    {
        immediate: true
    }
)

/**
 * 监听事件
 */
const listenEvents = () => {
    eventBus.on(WsEventEnum.SEND_MESSAGE, ({ message }) => {
        // 如果收到的消息为当前会话的消息
        if (message.message.roomId === sessionStore.sessionInfo.chattingId) {
            // 且当前处于最底部或者消息由本人发送
            if (
                bodyRef.value?.scrollTop + bodyRef.value?.clientHeight >=
                    bodyRef.value?.scrollHeight ||
                message.fromUser.uid === userStore.userInfo.uid
            ) {
                // 直接滚动到最底部
                scrollBottom()
            } else if (bodyRef.value?.scrollTop < bodyRef.value?.scrollHeight) {
                isShowNewMessage.value = true
            }
        }
    })
    bodyRef.value?.addEventListener(
        "scroll",
        _.throttle(() => {
            console.log("top: ", bodyRef.value?.scrollTop)
            console.log("height: ", bodyRef.value?.scrollHeight)
            if (
                bodyRef.value?.scrollTop + bodyRef.value?.clientHeight >=
                bodyRef.value?.scrollHeight - 10
            ) {
                isShowNewMessage.value = false
            }
        }, 500)
    )
}

/**
 * 取消监听事件
 */
const offEvents = () => {
    eventBus.off(WsEventEnum.SEND_MESSAGE)
}

onMounted(() => {
    listenEvents()
})

onBeforeUnmount(() => {
    offEvents()
})
</script>

<template>
    <div
        class="h-full bg-secondary overflow-x-hidden scroll-smooth"
        ref="bodyRef"
    >
        <div
            class="flex-center text-[#fff]"
            v-if="!messageStore.listPage.isLast"
        >
            <i
                class="iconfont icon-loading animate-spin"
                v-observe="messageStore.getMessageList"
            >
            </i>
            <span class="text-[13px] ml-[5px]">消息列表加载中</span>
        </div>
        <ChatMessageItemStrategy
            v-if="messageStore.listPage.isLast"
            :message="lastMessage"
        />
        <ChatMessageItemStrategy
            v-for="message in messageStore.messageList"
            :key="message?.message?.id"
            :message="message"
        />
        <div
            v-if="isShowNewMessage"
            class="absolute bottom-[60px] right-[30px] px-[8px] h-[25px] text-[8px] text-[#fff] flex-center rounded-full bg-primary cursor-pointer border-[2px] border-[#0094ff]"
            @click="scrollBottom"
        >
            <span class="text-[#0094ff] font-bold">
                {{ sessionStore.getSessionInfo.unreadCount }} </span
            >&nbsp;条新消息
        </div>
    </div>
</template>

<style scoped lang="scss"></style>