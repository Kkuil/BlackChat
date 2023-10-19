<script setup lang="ts">
import GroupAdvancedSetting from "@/layout/components/chat/ChatContent/components/components/components/GroupAdvancedSetting.vue"
import GroupBaseInfoSetting from "@/layout/components/chat/ChatContent/components/components/components/GroupBaseInfoSetting.vue"
</script>

<template>
    <div>
        <el-divider content-position="left" class="my-[15px]">
            基本信息
        </el-divider>
        <GroupBaseInfoSetting />
        <el-divider content-position="left" class="my-[15px]">高级</el-divider>
        <GroupAdvancedSetting />
    </div>
</template>