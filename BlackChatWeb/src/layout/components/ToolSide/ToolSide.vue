<script setup lang="ts">
import ToolSideTop from "@/layout/components/ToolSide/components/ToolSideTop.vue"
import ToolSideBottom from "@/layout/components/ToolSide/components/ToolSideBottom.vue"
</script>

<template>
    <div
        class="w-full h-full flex flex-row sm:flex-col py-[5px] px-[10px] sm:p-0"
    >
        <ToolSideTop class="w-full h-1/2" />
        <ToolSideBottom class="w-full h-1/2" />
    </div>
</template>

<style scoped></style>