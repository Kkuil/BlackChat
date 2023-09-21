<script setup lang="ts">
import { PROJECT_URL } from "@/constant/global"
</script>

<template>
    <div class="w-full h-full flex flex-col items-center justify-end">
        <a
            class="item flex-center flex-col cursor-pointer"
            :href="PROJECT_URL"
            target="_blank"
        >
            <i class="iconfont icon-github text-[35px]"></i>
            <span class="desc text-[#fff] text-[12px]">Github源码</span>
        </a>
    </div>
</template>

<style scoped lang="scss">
.item {
    &:hover {
        .desc {
            transition: all 0.3s;
            color: #0094ff !important;
        }
    }
}
</style>