<script setup lang="ts">
import ToolSide from "@/layout/components/ToolSide/ToolSide.vue"
</script>

<template>
    <div
        class="w-full sm:w-[75%] h-full sm:h-[650px] bg-primary rounded-[0px] sm:rounded-[15px] flex flex-col justify-between sm:flex-row overflow-hidden p-[15px]"
    >
        <ToolSide
            class="hidden md:flex md:flex-[10%] xl:flex-[7%] rounded-[10px] bg-secondary sm:bg-transparent"
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