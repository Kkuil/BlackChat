<script setup lang="ts">
import { ref } from "vue"
import { useRoute } from "vue-router"

const $route = useRoute()

/**
 * 当前选中的tab
 */
const selected = ref<string>($route.name as string)

/**
 * 切换tab
 * @param tab
 */
const switchTab = (tab: string) => {
    selected.value = tab
}
</script>

<template>
    <RouterLink
        class="w-[50px] h-[50px] py-[5px] rounded-[8px] cursor-pointer hover:bg-secondary flex-center mt-[10px] transition-[background-color]"
        :class="selected === 'chat' ? 'selected' : ''"
        @click="switchTab('chat')"
        to="/chat"
    >
        <i class="iconfont icon-message text-[28px]"></i>
    </RouterLink>
    <RouterLink
        class="w-[50px] h-[50px] py-[5px] rounded-[8px] cursor-pointer hover:bg-secondary flex-center mt-[10px] transition-[background-color]"
        :class="selected === 'contact' ? 'selected' : ''"
        @click="switchTab('contact')"
        to="/contact"
    >
        <i class="iconfont icon-contact text-[28px]"></i>
    </RouterLink>
</template>

<style scoped lang="scss">
.selected {
    background: #424656;
    color: #0094ff;
}
</style>