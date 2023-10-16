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
import eventBus from "@/utils/eventBus"
import { WsEventEnum } from "@/enums/websocket/WsEventEnum"

const userStore = useUserStore()
const messageStore = useMessageStore()
const sessionStore = useSessionStore()

const editorRef = ref<HTMLElement | null>()

const mode = ref<string>("text")

const activeMoreFunction = ref<boolean>(false)

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
}, 300)

/**
 * 取消回复
 */
const onCancelReply = () => {
    messageStore.cancelReply()
}

const mediaRecorder = ref<MediaRecorder>()

/**
 * 开始录制
 */
const startRecording = () => {
    console.log("start record")
    const constraints = { audio: true }
    navigator.mediaDevices
        .getUserMedia(constraints)
        .then(function (stream) {
            console.log(stream)
            mediaRecorder.value = new MediaRecorder(stream)
            mediaRecorder.value?.start()
        })
        .catch(function (err) {
            console.log("无法获取用户媒体设备:", err)
        })
}

/**
 * 停止录制
 */
function stopRecording() {
    console.log("stop record")
    mediaRecorder.value?.stop()
    console.log(mediaRecorder)
}

/**
 * 录制声音
 */
const voiceHandler = () => {
    if (mediaRecorder.value && mediaRecorder.value?.state === "recording") {
        stopRecording()
    } else {
        startRecording()
    }
}

eventBus.on(WsEventEnum.AITE, ({ people }) => {
    console.log(people)
})
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
            v-if="userStore.isTempUser"
            :class="userStore.isTempUser ? 'backdrop-blur-md' : ''"
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
            v-if="mode === 'text'"
            class="hidden md:flex md:flex-[15%] lg:flex-[2%] cursor-pointer hover:bg-secondary rounded-[5px] transition-[background-color] mr-[3px]"
            icon-class="voice"
            @click="mode = 'voice'"
        />
        <SvgIcon
            v-else
            class="hidden md:flex md:flex-[15%] lg:flex-[2%] cursor-pointer hover:bg-secondary rounded-[5px] transition-[background-color] mr-[3px]"
            icon-class="keyboard"
            @click="mode = 'text'"
        />
        <div
            class="editor flex-[89%] flex items-center md:flex-[87%] lg:flex-[75%] h-[75%] px-[5px] rounded-[6px] transition-[border] border-[1px] border-[transparent]"
        >
            <input
                v-if="mode === 'text'"
                class="w-full outline-0 bg-[transparent] focus:border-[#0094ff] hover:border-[#0094ff]"
                ref="editorRef"
                :class="
                    messageStore.messageInfo.body.content
                        ? 'border-[#0094ff]'
                        : ''
                "
                placeholder="我们期待您的发言，请文明发言"
                :value="messageStore.messageInfo.body.content"
                @input="typing"
                @keyup.enter="send"
                contenteditable="true"
            />
            <div
                v-else
                class="w-full flex-center cursor-pointer"
                @click="voiceHandler"
            >
                点击录制
            </div>
        </div>
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