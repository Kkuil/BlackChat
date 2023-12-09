<script setup lang="ts">
import TextContent from "@/components/ChatMessageItem/components/TextContent/TextContent.vue"
import { MessageTypeEnum } from "@/enums/MessageTypeEnum"
import ImageContent from "@/components/ChatMessageItem/components/ImageContent/ImageContent.vue"
import SoundContent from "@/components/ChatMessageItem/components/SoundContent/SoundContent.vue"
import VideoContent from "@/components/ChatMessageItem/components/VideoContent/VideoContent.vue"
import ContextMenuContainer from "@/components/ContextMenuContainer/ContextMenuContainer.vue"
import { ref } from "vue"
import moment from "moment"
import { Avatar } from "@element-plus/icons-vue"
import FileContent from "@/components/ChatMessageItem/components/FileContent/FileContent.vue"
import type { ChatMessageResp } from "@/layout/components/chat/ChatContent/ChatMessageResp.d.ts"
import { useUserStore } from "@/stores/user"

const USER_ITEMS = ["aite", "add-friend"]
const MESSAGE_ITEMS = ["recall", "reply"]

defineProps<{
    message: ChatMessageResp.ChatMessageBaseResp<any, any>
    direction: "left" | "right"
}>()

const userStore = useUserStore()

const menuOptions = ref({ x: 0, y: 0 })
const isShowMenu = ref<boolean>(false)
const items = ref<string[]>([])

/** 右键菜单 */
const handleUserRightClick = (e: MouseEvent, list: string[]) => {
    const { x, y } = e
    menuOptions.value.x = x
    menuOptions.value.y = y
    isShowMenu.value = true
    items.value = list
}

/**
 * 多选
 */
const handleMultiSelect = (
    message: ChatMessageResp.ChatMessageBaseResp<any, any>
) => {
    console.log(message)
}
</script>

<template>
    <div
        class="message flex items-center justify-between"
        :class="direction == 'left' ? 'flex-row' : 'flex-row-reverse'"
    >
        <div
            class="w-[90%] h-auto flex items-start my-[10px]"
            :class="
                direction == 'right'
                    ? 'flex-row-reverse justify-end float-right'
                    : 'flex-row'
            "
        >
            <el-avatar
                :src="
                    userStore.userInfoCache[message.fromUser.uid + '']?.avatar
                "
                size="default"
                class="cursor-pointer"
                @contextmenu.prevent.stop="
                    handleUserRightClick($event, USER_ITEMS)
                "
            >
                <el-icon :size="20">
                    <Avatar />
                </el-icon>
            </el-avatar>
            <div
                class="flex w-[90%] flex-col mx-[7px]"
                :class="direction == 'left' ? 'items-start' : 'items-end'"
            >
                <h3
                    class="user flex text-[12px] text-[#ccc] mb-[5px]"
                    :class="
                        direction == 'left' ? 'flex-row' : 'flex-row-reverse'
                    "
                >
                    <span> ({{ message?.fromUser.place }}) </span>
                    <span class="username">
                        {{
                            userStore.userInfoCache[message.fromUser.uid + ""]
                                ?.name
                        }}
                    </span>
                    <span
                        class="opacity-0 hover:opacity-100 transition-[opacity] send-time mx-[5px]"
                    >
                        {{ moment(message?.message?.sendTime).format("HH:mm") }}
                    </span>
                </h3>
                <TextContent
                    v-if="message?.message?.type == MessageTypeEnum.TEXT"
                    :body="message.message.body"
                    class="rounded-b-[15px] text-[#fff] text-[14px] p-[7px] max-w-[100%] break-words"
                    :class="
                        direction == 'left'
                            ? 'rounded-tr-[15px] bg-third'
                            : 'rounded-tl-[15px] bg-[#1d90f5]'
                    "
                    @contextmenu.prevent.stop="
                        handleUserRightClick($event, MESSAGE_ITEMS)
                    "
                />
                <ImageContent
                    v-else-if="message?.message?.type == MessageTypeEnum.IMAGE"
                    :body="message?.message?.body"
                    @contextmenu.prevent.stop="
                        handleUserRightClick($event, MESSAGE_ITEMS)
                    "
                />
                <FileContent
                    v-else-if="message?.message?.type == MessageTypeEnum.FILE"
                    :body="message?.message?.body"
                    @contextmenu.prevent.stop="
                        handleUserRightClick($event, MESSAGE_ITEMS)
                    "
                />
                <SoundContent
                    v-else-if="message?.message?.type == MessageTypeEnum.SOUND"
                    :body="message?.message?.body"
                    @contextmenu.prevent.stop="
                        handleUserRightClick($event, MESSAGE_ITEMS)
                    "
                />
                <VideoContent
                    v-else-if="message?.message?.type == MessageTypeEnum.VIDEO"
                    :body="message?.message?.body"
                    @contextmenu.prevent.stop="
                        handleUserRightClick($event, MESSAGE_ITEMS)
                    "
                />
                <div
                    v-if="message?.message?.reply"
                    class="px-[5px] flex items-end justify-between max-w-[75%] p-[2px] text-[#f5f5f5] text-[13px] mt-[5px] bg-[#777] rounded-[4px] cursor-pointer transition-[background-color] hover:bg-opacity-[80%]"
                    title="回到原文"
                >
                    <i
                        class="iconfont icon-return-message mr-[5px] text-[12px]"
                    ></i>
                    <div
                        class="w-full whitespace-nowrap overflow-ellipsis overflow-hidden"
                    >
                        {{ message?.message?.reply.name }}:
                        {{ message?.message?.reply.body }}
                    </div>
                </div>
                <ContextMenuContainer
                    v-if="userStore.isLogin"
                    v-model:show="isShowMenu"
                    :message="message"
                    :options="menuOptions"
                    :items="items"
                    @multi-select="handleMultiSelect"
                />
            </div>
        </div>
        <!--        <div class="selection h-full mx-[3px]">-->
        <!--            <el-checkbox size="large" @change="checkedMessage" />-->
        <!--        </div>-->
    </div>
</template>

<style scoped lang="scss"></style>