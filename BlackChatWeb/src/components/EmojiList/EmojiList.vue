<script setup lang="ts">
const emojis = [
    "😀",
    "😃",
    "😄",
    "😁",
    "😆",
    "😅",
    "😂",
    "🤣",
    "😊",
    "😇",
    "🙂",
    "🙃",
    "😉",
    "😌",
    "😍",
    "😘",
    "😗",
    "😙",
    "😚",
    "😋",
    "😛",
    "😝",
    "😜",
    "🤪",
    "🤨",
    "🧐",
    "🤓",
    "😎",
    "🤩",
    "😏",
    "😒",
    "😞",
    "😔",
    "😟",
    "😕",
    "🙁",
    "😣",
    "😖",
    "😫",
    "😩",
    "😢",
    "😭",
    "😮",
    "💨",
    "😤",
    "😠",
    "😡",
    "🤬",
    "🤯",
    "😳",
    "😱",
    "😨",
    "😰",
    "😥",
    "😓",
    "🤗",
    "🤔",
    "🤭",
    "🤫",
    "🤥",
    "😶",
    "🌫",
    "😐",
    "😑",
    "😬",
    "🙄",
    "😯",
    "😦",
    "😧",
    "😮",
    "😲",
    "😴",
    "🤤",
    "😪",
    "😵",
    "💫",
    "🤐",
    "🤢",
    "🤮",
    "🤧",
    "😷",
    "🤒",
    "🤕",
    "🤑",
    "🤠",
    "😈",
    "👿",
    "👹",
    "👺",
    "🤡",
    "💩",
    "👻",
    "💀",
    "👽",
    "👾",
    "🤖",
    "🎃",
    "😺",
    "😸",
    "😹",
    "😻",
    "😼",
    "😽",
    "🙀",
    "😿",
    "😾",
    "🐶"
]

const emits = defineEmits<{
    (e: "select", emoji: string): void
}>()
</script>

<template>
    <ul class="emoji-list flex flex-wrap">
        <li
            class="emoji-item"
            v-for="(emoji, index) in emojis"
            :key="index"
            @click="emits('select', emoji)"
        >
            {{ emoji }}
        </li>
    </ul>
</template>

<style scoped lang="scss">
.emoji-item {
    cursor: pointer;
    font-size: 23px;
    border-radius: 5px;

    &:hover {
        background-color: #ccc;
    }
}
</style>