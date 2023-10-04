<script setup lang="ts">
import { ref } from "vue"
import { sendMessage } from "@/api/chat"
import { useUserStore } from "@/stores/user"
import { popUpLoginDialog } from "@/utils/popDialog/popLoginDialog"
import { useMessageStore } from "@/stores/message"
import { MessageTypeEnum } from "@/enums/MessageTypeEnum"
import ChatEmoji from "@/components/ChatEmoji/ChatEmoji.vue"
import { ElMessage } from "element-plus"
import _ from "lodash"
import { useSessionStore } from "@/stores/session"
import MoreFunctions from "@/layout/components/chat/ChatContent/components/MoreFunctions.vue"

const userStore = useUserStore()
const messageStore = useMessageStore()
const sessionStore = useSessionStore()

/**
 * 打字中
 * @param e
 */
const typing = (e: Event & { target: HTMLInputElement }) => {
    const isText = messageStore.messageInfo.messageType === MessageTypeEnum.TEXT
    if (isText) {
        messageStore.typing(e.target.value)
    }
}

/**
 * 发送消息
 */
const send = _.throttle(async () => {
    if (!messageStore.messageInfo.body.content) {
        return ElMessage.error("消息不能为空")
    }
    const result = await sendMessage({
        ...messageStore.messageInfo,
        roomId: sessionStore.sessionInfo.chattingId
    })
    if (!result.data) {
        return
    }
    messageStore.addMessage(result.data)
}, 300)

/**
 * 取消回复
 */
const onCancelReply = () => {
    messageStore.cancelReply()
}

const activeMoreFunction = ref<boolean>(false)
</script>

<template>
    <div
        class="chat-input h-[40px] flex px-[10px] items-center bg-third rounded-[10px] text-[#fff] text-[12px] relative"
        :class="activeMoreFunction ? 'rounded-b-[0px]' : ''"
    >
        <div
            class="recall w-full h-[25px] overflow-hidden absolute top-[-30px] left-0 rounded-full bg-[#777] flex items-center justify-between px-[10px] cursor-pointer hover:bg-opacity-80 transition-[background-color]"
            v-if="messageStore.replyMessage.id"
            title="回到原文"
        >
            <div>
                <i class="iconfont icon-reply text-[11px]"></i>
                <span class="ml-[10px]">
                    <span>{{ messageStore.replyMessage.name }}: </span>
                    <span>{{ messageStore.replyMessage.content }}</span>
                </span>
            </div>
            <i class="iconfont icon-close" @click="onCancelReply"></i>
        </div>
        <div
            v-if="!userStore.userInfo.name"
            :class="!userStore.userInfo.name ? 'backdrop-blur-md' : ''"
            class="w-full h-full absolute top-0 left-0 flex-center text-[12px] font-serif rounded-[10px]"
        >
            点击
            <a
                class="text-[#0094ff] text-sm cursor-pointer"
                @click="popUpLoginDialog"
            >
                登录
            </a>
            后再进行发言吧
        </div>
        <SvgIcon
            class="flex-[1%] md:flex-[15%] lg:flex-[2%] cursor-pointer hover:bg-secondary rounded-[5px] transition-[background-color] mr-[3px]"
            icon-class="voice"
            title="点击切换语音"
        />
        <input
            class="flex-[89%] md:flex-[87%] lg:flex-[75%] h-[75%] outline-0 px-[5px] bg-[transparent] rounded-[6px] transition-[border] border-[1px] border-[transparent] focus:hover:border-[1px] focus:border-[#0094ff] hover:border-[#0094ff]"
            placeholder="我们期待您的发言，请文明发言"
            :value="messageStore.messageInfo.body.content"
            @input="typing"
            @keyup.enter="send"
        />
        <div class="flex-center flex-[6%] ml-[10px]">
            <i
                title="更多功能"
                class="flex-center flex-1 iconfont icon-more-functions text-[18px] w-[30px] h-[30px] rounded-[5px] cursor-pointer hover:bg-secondary"
                @click="activeMoreFunction = !activeMoreFunction"
            ></i>
            <el-popover placement="top" width="400" trigger="click">
                <template #reference>
                    <SvgIcon
                        class="flex-[1] rounded-[5px] cursor-pointer hover:bg-secondary"
                        icon-class="expression"
                    />
                </template>
                <template #default>
                    <ChatEmoji />
                </template>
            </el-popover>
            <i
                title="发送"
                class="flex-center flex-1 iconfont icon-send text-[18px] w-[30px] h-[30px] rounded-[5px] cursor-pointer hover:bg-secondary transition-[transform]"
                :class="
                    messageStore.messageInfo.body &&
                    messageStore.messageInfo.body?.content &&
                    messageStore.messageInfo.messageType == MessageTypeEnum.TEXT
                        ? 'rotate-[45deg]'
                        : ''
                "
                @click="send"
            ></i>
        </div>
    </div>
    <MoreFunctions
        :activeMoreFunction="activeMoreFunction"
        :close="() => (activeMoreFunction = false)"
    />
</template>

<style lang="scss"></style>