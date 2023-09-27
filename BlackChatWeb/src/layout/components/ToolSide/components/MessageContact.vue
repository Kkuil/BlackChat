<script setup lang="ts">
import { ref } from "vue"

/**
 * 当前选中的tab
 */
const selected = ref<string>("contact")

/**
 * 切换tab
 * @param tab
 */
const switchTab = (tab: string) => {
    selected.value = tab
}
</script>

<template>
    <div
        class="w-[50px] h-[50px] py-[5px] rounded-[8px] cursor-pointer hover:bg-secondary flex-center mt-[10px] transition-[background-color]"
        :class="selected === 'message' ? 'selected' : ''"
        @click="switchTab('message')"
    >
        <i class="iconfont icon-message text-[28px]"></i>
    </div>
    <div
        class="w-[50px] h-[50px] py-[5px] rounded-[8px] cursor-pointer hover:bg-secondary flex-center mt-[10px] transition-[background-color]"
        :class="selected === 'contact' ? 'selected' : ''"
        @click="switchTab('contact')"
    >
        <i class="iconfont icon-contact text-[28px]"></i>
    </div>
</template>

<style scoped lang="scss">
.selected {
    background: #424656;
}
</style>