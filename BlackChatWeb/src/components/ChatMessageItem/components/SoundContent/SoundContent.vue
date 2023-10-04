<script setup lang="ts">
import SoundMessageBody = ChatMessageResp.SoundMessageBody

defineProps<{
    body: SoundMessageBody
}>()
</script>

<template>
    <div
        class="message rounded-[5px] text-[14px] p-[7px] bg-[#f4f4f4] hover:bg-opacity-80 cursor-pointer"
        title="点击播放"
    >
        <i class="iconfont icon-sound"></i>
        <span>{{ body.second }} s''</span>
    </div>
</template>

<style scoped lang="scss"></style>