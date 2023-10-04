<script setup lang="ts">
import { ElMessage } from "element-plus"
import _ from "lodash"

const props = defineProps<{
    content: string
}>()

const copy = _.throttle(() => {
    const textarea = document.createElement("textarea")
    textarea.value = props.content
    document.body.appendChild(textarea)
    textarea.select()
    document.execCommand("copy")
    document.body.removeChild(textarea)
    ElMessage.success("复制成功")
}, 500)
</script>

<template>
    <div @dblclick="copy">
        {{ content }}
    </div>
</template>

<style scoped lang="scss"></style>