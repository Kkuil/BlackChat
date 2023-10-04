<script setup lang="ts">
import { onMounted, onUnmounted, ref } from "vue"
import Player from "xgplayer"
import PresetPlayer from "xgplayer"
import ScreenShot from "xgplayer/es/plugins/screenShot"
import VideoMessageBody = ChatMessageResp.VideoMessageBody

const props = defineProps<{
    body: VideoMessageBody
}>()

const player = ref<Player>()

const number = Math.random() * 10

const init = () => {
    if (!props.body.url) return
    if (player.value) {
        if (player.value instanceof PresetPlayer) {
            player.value.destroy()
        }
    }
    player.value = new Player({
        id: "video" + number,
        url: props.body.url,
        width: "70%",
        fitVideoSize: "fixWidth",
        videoInit: true, // 显示首帧
        miniplayer: true,
        miniplayerConfig: {
            width: 320,
            height: 180
        },
        pip: true, // 画中画
        cssFullscreen: true, // 全屏样式
        screenShot: {
            saveImg: true
        },
        ignores: ["replay", "fullscreen"],
        lang: "zh-cn",
        plugins: [ScreenShot]
    })
}

onMounted(() => {
    init()
})

onUnmounted(() => {
    if (player.value) {
        if (player.value instanceof PresetPlayer) {
            player.value.destroy()
        }
    }
})
</script>

<template>
    <div :id="`video${number}`" class="rounded-[10px]"></div>
</template>

<style scoped lang="scss"></style>