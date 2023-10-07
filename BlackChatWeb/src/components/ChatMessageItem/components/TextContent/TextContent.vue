<script setup lang="ts">
import { ElMessage } from "element-plus"
import _ from "lodash"
import { computed } from "vue"
import type { ChatMessageResp } from "@/layout/components/chat/ChatContent/ChatMessageResp"

const props = defineProps<{
    body: ChatMessageResp.TextMessageBody
}>()

const showContent = computed(() => {
    const urlContentMap = props.body.urlContentMap
    console.log("urlContentMap", urlContentMap)
    let content = props.body.content
    if (urlContentMap) {
        for (const address in urlContentMap) {
            const card = `<div>
                            <span>${address}</span>
                            <a class="flex px-[10px] py-[15px] w-full h-[60px] rounded-[10px] bg-third my-[3px] cursor-pointer hover:shadow-[#fff]" href="${address}" target="_blank">
                                <img
                                    src="${urlContentMap[address].image}"
                                    alt="${urlContentMap[address].title}"
                                />
                                <h1 class="text-[#fff] text-[13px] ml-[10px]">${urlContentMap[address].title}</h1>
                            </a>
                        </div>`
            const regex = new RegExp(address, "gi")
            content = content.replace(regex, card)
        }
    }
    return content
})

/**
 * 复制
 */
const copy = _.throttle(() => {
    const textarea = document.createElement("textarea")
    textarea.value = props.body.content
    document.body.appendChild(textarea)
    textarea.select()
    document.execCommand("copy")
    document.body.removeChild(textarea)
    ElMessage.success("复制成功")
}, 500)
</script>

<template>
    <div @dblclick="copy" v-html="showContent"></div>
</template>

<style scoped lang="scss"></style>