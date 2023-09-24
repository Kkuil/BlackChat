<script setup lang="ts"></script>

<template>
    <div
        class="h-full flex px-[10px] items-center bg-third rounded-[10px] text-[#fff] text-[12px]"
    >
        <SvgIcon
            class="flex-[5%] md:flex-[15%] lg:flex-[5%] cursor-pointer hover:bg-secondary rounded-[5px] transition-[background-color] mr-[3px]"
            icon-class="voice"
            title="点击切换语音"
        />
        <input
            class="flex-[65%] md:flex-[85%] lg:flex-[65%] h-[75%] outline-0 px-[5px] bg-[transparent] rounded-[6px] transition-[border] border-[1px] border-[transparent] focus:hover:border-[1px] focus:border-[#0094ff] hover:border-[#0094ff]"
            placeholder="我们期待您的发言，说点什么吧"
        />
        <div class="flex flex-[25%] md:hidden lg:flex">
            <SvgIcon class="flex-[1] cursor-pointer" icon-class="expression" />
            <SvgIcon class="flex-[1] cursor-pointer" icon-class="aite" />
            <SvgIcon class="flex-[1] cursor-pointer" icon-class="picture" />
            <SvgIcon class="flex-[1] cursor-pointer" icon-class="file" />
            <SvgIcon class="flex-[1] cursor-pointer" icon-class="send" />
        </div>
    </div>
</template>

<style scoped lang="scss"></style>
