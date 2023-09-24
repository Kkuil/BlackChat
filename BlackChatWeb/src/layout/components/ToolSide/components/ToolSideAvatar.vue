<script setup lang="ts">
import { Avatar } from "@element-plus/icons-vue"
import { ToolSide } from "@/layout/components/ToolSide/components/type"

defineProps<ToolSide.AvatarTypeProps>()
</script>
<template>
    <div class="cursor-pointer flex-center flex-col">
        <el-avatar :size="50" :src="avatar">
            <el-icon :size="25">
                <Avatar />
            </el-icon>
        </el-avatar>
        <span class="mt-[7px] text-[#fff] text-[14px] hidden sm:inline-block">
            {{ name }}
        </span>
    </div>
</template>

<style scoped></style>