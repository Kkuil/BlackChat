<script setup lang="ts">
import ImageMessageBody = ChatMessageResp.ImageMessageBody

const props = defineProps<{
    body: ImageMessageBody
}>()
const srcList = [props.body.url]
</script>
<template>
    <el-image
        title="点击预览"
        class="w-[50%] rounded-[8px] cursor-pointer"
        :src="body.url"
        :zoom-rate="1.2"
        :initial-index="4"
        :preview-src-list="srcList"
        fit="cover"
    />
</template>

<style scoped lang="scss"></style>