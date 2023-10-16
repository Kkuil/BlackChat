<script setup lang="ts">
import ToolSide from "@/layout/components/ToolSide/ToolSide.vue"
</script>

<template>
    <div
        class="w-full sm:w-[85%] h-full sm:h-[95%] sm:bg-primary rounded-[0px] sm:rounded-[15px] flex flex-col justify-between sm:flex-row overflow-hidden p-[15px]"
    >
        <ToolSide
            class="flex-[7%] mb-[10px] sm:mb-0 sm:h-full sm:flex-[15%] md:flex md:flex-[10%] xl:flex-[6%] rounded-[10px] bg-secondary sm:bg-transparent"
        />
        <RouterView></RouterView>
    </div>
</template>

<style scoped lang="scss">
.home-enter-active,
.home-leave-active {
    transition: opacity 0.3s;
}

.home-enter-to,
.home-leave-from {
    opacity: 1;
}

.home-enter-from,
.home-leave-to {
    opacity: 0;
}
</style>