<script setup lang="ts">
import TextContent from "@/components/ChatMessageItem/components/TextContent/TextContent.vue"
import { MessageTypeEnum } from "@/enums/MessageTypeEnum"
import ImageContent from "@/components/ChatMessageItem/components/ImageContent/ImageContent.vue"
import SoundContent from "@/components/ChatMessageItem/components/SoundContent/SoundContent.vue"
import VideoContent from "@/components/ChatMessageItem/components/VideoContent/VideoContent.vue"
import ContextMenu from "@/components/ContextMenuContainer/ContextMenuContainer.vue"
import { ref } from "vue"
import moment from "moment"

const USER_ITEMS = ["aite", "add-friend"]
const MESSAGE_ITEMS = ["recall", "reply"]

defineProps<{
    message: ChatMessageResp.ChatMessageBaseResp<any, any>
    direction: "left" | "right"
}>()

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
</script>

<template>
    <div
        class="w-[92%] h-auto flex items-start my-[10px]"
        :class="
            direction == 'right'
                ? 'flex-row-reverse justify-end float-right'
                : 'flex-row'
        "
    >
        <el-avatar
            :src="message.fromUser.avatar"
            size="default"
            class="cursor-pointer"
            @contextmenu.prevent.stop="handleUserRightClick($event, USER_ITEMS)"
        />
        <div
            class="flex flex-[1] flex-col mx-[7px]"
            :class="direction == 'left' ? 'items-start' : 'items-end'"
        >
            <h3
                class="user flex text-[12px] text-[#ccc] mb-[5px]"
                :class="direction == 'left' ? 'flex-row' : 'flex-row-reverse'"
            >
                <span>
                    &nbsp;({{
                        message.fromUser.ipInfo.updateIpDetail.city
                    }})&nbsp;
                </span>
                <span class="username">{{ message.fromUser.name }}</span>
                <span
                    class="opacity-0 hover:opacity-100 transition-[opacity] send-time mx-[5px]"
                >
                    {{ moment(message.message.sendTime).format("HH:mm") }}
                </span>
            </h3>
            <TextContent
                v-if="message.message.type == MessageTypeEnum.TEXT"
                :content="message.message.body.content"
                class="rounded-b-[15px] text-[#fff] text-[14px] p-[5px]"
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
                v-else-if="message.message.type == MessageTypeEnum.IMAGE"
                :body="message.message.body"
                @contextmenu.prevent.stop="
                    handleUserRightClick($event, MESSAGE_ITEMS)
                "
            />
            <FileContent
                v-else-if="message.message.type == MessageTypeEnum.FILE"
                :body="message.message.body"
                @contextmenu.prevent.stop="
                    handleUserRightClick($event, MESSAGE_ITEMS)
                "
            />
            <SoundContent
                v-else-if="message.message.type == MessageTypeEnum.SOUND"
                :body="message.message.body"
                @contextmenu.prevent.stop="
                    handleUserRightClick($event, MESSAGE_ITEMS)
                "
            />
            <VideoContent
                v-else-if="message.message.type == MessageTypeEnum.VIDEO"
                :body="message.message.body"
                @contextmenu.prevent.stop="
                    handleUserRightClick($event, MESSAGE_ITEMS)
                "
            />
            <div
                v-if="message.message.reply"
                class="recall p-[2px] text-[#f5f5f5] text-[8px] mt-[5px] bg-[#777] rounded-[4px] cursor-pointer transition-[background-color] hover:bg-opacity-[80%]"
                title="回到原文"
            >
                <span class="username">
                    {{ message.message.reply.name }}:
                </span>
                <span class="content">
                    {{ message.message.reply.body }}
                </span>
                <i
                    class="iconfont icon-return-message ml-[10px] text-[12px]"
                ></i>
            </div>
            <ContextMenu
                v-model:show="isShowMenu"
                :message="message"
                :options="menuOptions"
                :items="items"
            />
        </div>
    </div>
</template>

<style scoped lang="scss"></style>