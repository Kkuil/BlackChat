<script setup lang="ts">
import { ref } from "vue"
import DeadZone from "@/layout/components/chat/ChatContent/components/components/DeadZone.vue"
import GeneralZone from "@/layout/components/chat/ChatContent/components/components/GeneralZone.vue"

const activeTab = ref<string>("general")
</script>

<template>
    <el-tabs type="border-card" v-model="activeTab">
        <el-tab-pane label="正常区" name="general">
            <GeneralZone />
        </el-tab-pane>
        <el-tab-pane label="危险区" name="dead-zone">
            <DeadZone />
        </el-tab-pane>
    </el-tabs>
</template>

<style scoped lang="scss"></style>