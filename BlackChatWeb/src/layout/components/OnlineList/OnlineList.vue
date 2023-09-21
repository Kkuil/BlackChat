<script setup lang="ts">
import { ref } from "vue"

const onlineList = ref([])
</script>

<template>
    <div
        class="online-list flex flex-col ml-[10px] bg-secondary rounded-[10px] text-[#fff] p-[10px]"
    >
        <h1 class="online-count flex items-center h-[30px] text-[14px]">
            在线人数：{{ onlineList.length }}
        </h1>
        <ul class="flex-1 overflow-y-scroll">
            <li
                class="item flex items-center py-[7px] cursor-pointer mb-[5px] hover:bg-[#263242] rounded-[5px] px-[7px]"
                v-for="index in 30"
                :key="index"
            >
                <el-avatar :size="'small'" />
                <span class="text-[14px] ml-[5px]">Kkuil</span>
            </li>
        </ul>
    </div>
</template>

<style scoped></style>