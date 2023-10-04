<script setup lang="ts">
import SvgIcon from "@/components/SvgIcon/SvgIcon.vue"
import { computed } from "vue"

const props = defineProps<{
    body: ChatMessageResp.FileMessageBody
}>()

const ext = computed(() => props.body.fileName.split(".")[1])
</script>

<template>
    <a
        class="block w-[50%] h-[80px] rounded-[10px] bg-[#f4f4f4] p-[10px] cursor-pointer"
        title="点击下载"
        :href="props.body.url"
        download="props.body.fileName"
    >
        <div
            class="h-full flex justify-between items-center px-[10px] bg-[#fff] rounded-[10px]"
        >
            <SvgIcon :icon-class="ext" class="text-[25px]" />
            <div class="flex flex-col text-[12px]">
                <h2
                    class="w-full line-clamp-1 overflow-hidden overflow-ellipsis"
                >
                    文件名：{{ body.fileName }}
                </h2>
                <h3
                    class="w-full line-clamp-1 overflow-hidden overflow-ellipsis"
                >
                    文件大小：{{ body.size }}字节
                </h3>
            </div>
            <i class="iconfont icon-download text-[20px]"></i>
        </div>
    </a>
</template>

<style scoped lang="scss"></style>