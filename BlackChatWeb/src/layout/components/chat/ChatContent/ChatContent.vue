<script setup lang="ts">
import ChatInput from "@/layout/components/chat/ChatContent/components/ChatInput.vue"
import ChatBody from "@/layout/components/chat/ChatContent/components/ChatBody.vue"
import { useSessionStore } from "@/stores/session"
import { ref } from "vue"
import OnlineList from "@/layout/components/chat/OnlineList/OnlineList.vue"
import SettingBox from "@/layout/components/chat/ChatContent/components/SettingBox.vue"

const sessionStore = useSessionStore()
// 响应式布局控制在线列表的显示和隐藏
const isShowOnlineList = ref<boolean>(false)
// 设置
const isShowSetting = ref<boolean>(false)
</script>

<template>
    <div
        class="relative w-full h-full flex flex-col bg-secondary rounded-[10px] overflow-hidden"
    >
        <div
            class="w-full h-[8%] bg-third flex items-center justify-between px-[10px] text-[#f5f5f5] font-extrabold"
        >
            <h1>{{ sessionStore.getSessionInfo.name }}</h1>
            <i
                class="iconfont icon-setting cursor-pointer text-[20px] text-[#fff]"
                title="设置"
                v-if="
                    sessionStore.isGroup &&
                    sessionStore.getSessionInfo.hotFlag !== 1
                "
                @click="isShowSetting = true"
            >
            </i>
        </div>
        <div class="px-[15px] py-[10px] h-[92%] flex flex-col">
            <ChatBody class="flex-1 overflow-y-scroll mb-[10px]" />
            <ChatInput />
        </div>
        <i
            v-if="sessionStore.isGroup"
            class="xl:hidden iconfont icon-arrow-left flex-center hover:justify-center justify-start text-[20px] cursor-pointer absolute top-[120px] right-[-20px] w-[35px] h-[35px] rounded-full bg-[#fff] text-[#000] transition-all transition- hover:translate-x-[-20px]"
            @click="isShowOnlineList = true"
        ></i>
        <el-drawer
            v-model="isShowOnlineList"
            direction="rtl"
            custom-class="w-full overflow-y-scroll md:w-1/2 lg:w-[30%]"
        >
            <template #header>
                <h4 class="text-[#f5f5f5]">成员列表</h4>
            </template>
            <template #default>
                <online-list class="h-full" />
            </template>
        </el-drawer>
        <el-drawer
            v-model="isShowSetting"
            direction="rtl"
            custom-class="w-full overflow-y-scroll md:w-1/2 lg:w-[30%]"
        >
            <template #header>
                <h4 class="text-[#f5f5f5]">设置</h4>
            </template>
            <template #default>
                <SettingBox />
            </template>
        </el-drawer>
    </div>
</template>