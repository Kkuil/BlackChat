<script setup lang="ts">
import EmojiList from "@/components/EmojiList/EmojiList.vue"
import SvgIcon from "@/components/SvgIcon/SvgIcon.vue"
</script>

<template>
    <div class="chat-emoji">
        <div class="data w-full h-[300px] overflow-y-scroll">
            <EmojiList />
        </div>
        <div class="flex tab mt-[10px]">
            <svg-icon
                icon-class="expression"
                class="w-[25px] h-[25px] flex-center hover:bg-secondary rounded-[5px] cursor-pointer mr-[8px]"
            />
        </div>
    </div>
</template>

<style scoped lang="scss"></style>