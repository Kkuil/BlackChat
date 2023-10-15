<script setup lang="ts">
import { PROJECT_URL } from "@/constant/global"
import ReceiveMessage from "@/layout/components/ToolSide/components/ReceiveMessage.vue"
</script>

<template>
    <div
        class="w-full h-full flex flex-row sm:flex-col items-center justify-end"
    >
        <ReceiveMessage />
        <a
            class="item flex-center flex-col cursor-pointer ml-[10px] sm:ml-0"
            :href="PROJECT_URL"
            target="_blank"
        >
            <i class="iconfont icon-github text-[25px] sm:text-[35px]"></i>
            <span class="desc text-[#fff] text-[12px] hidden sm:inline-block">
                Github源码
            </span>
        </a>
    </div>
</template>

<style lang="scss">
.item {
    &:hover {
        .desc {
            transition: all 0.3s;
            color: #0094ff !important;
        }
    }
}

.el-drawer {
    background-color: #272e3d;

    .el-drawer__header {
        margin-bottom: 10px;
    }

    .el-drawer__body {
        padding-top: 0;
    }
}

.el-tabs__item {
    color: #fff;
}
</style>