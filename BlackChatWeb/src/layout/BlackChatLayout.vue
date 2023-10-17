<script setup lang="ts">
import BlackChatMain from "./BlackChatMain.vue"
import { ref } from "vue"
import ChatList from "@/layout/components/chat/ChatList/ChatList.vue"

const isShowContactList = ref<boolean>(false)
</script>

<template>
    <div class="w-screen h-screen overflow-hidden flex-center">
        <BlackChatMain />
        <i
            class="md:hidden iconfont icon-arrow-right flex-center hover:justify-center justify-end text-[20px] cursor-pointer absolute top-[120px] left-[-20px] w-[35px] h-[35px] rounded-full bg-[#fff] text-[#000] transition-all transition- hover:translate-x-[20px]"
            @click="isShowContactList = true"
        ></i>
        <el-drawer
            v-model="isShowContactList"
            direction="ltr"
            custom-class="w-full overflow-y-scroll md:w-1/2 lg:w-[30%]"
        >
            <template #header>
                <h4 class="text-[#f5f5f5]">会话列表</h4>
            </template>
            <template #default>
                <chat-list class="h-full" />
            </template>
        </el-drawer>
    </div>
</template>