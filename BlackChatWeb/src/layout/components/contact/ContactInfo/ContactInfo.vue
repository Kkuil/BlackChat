<script setup lang="ts">
defineProps<{
    user: Partial<{
        id: string
        name: string
        avatar: string
    }>
}>()
</script>

<template>
    <div class="contact-info w-full h-full px-[30px]">
        <div class="flex user-info">
            <el-avatar :src="user.avatar" :size="85" />
            <div class="h-full info ml-[10px] font-sans">
                <h1 class="text-[17px] text-[#f5f5f5] mb-[5px]">
                    用户名：{{ user.name }}
                </h1>
                <h2 class="text-[14px] text-[#666] mb-[5px]">
                    uid: {{ user.id }}
                </h2>
                <h1 class="text-[14px] text-[#666] mb-[5px]">
                    地区：{{ user.name }}
                </h1>
            </div>
            <div
                class="ml-auto flex flex-col items-center justify-between operation"
            >
                <el-button size="small" type="primary" class="w-[100px]">
                    发消息
                </el-button>
                <el-button size="small" type="danger" class="w-[100px] m-0">
                    删除
                </el-button>
                <el-button
                    size="small"
                    type="text"
                    class="bg-secondary w-[100px] m-0"
                >
                    拉黑
                </el-button>
            </div>
        </div>
        <el-divider content-position="center" />
    </div>
</template>

<style scoped lang="scss"></style>