<script setup lang="ts">
import { ref } from "vue"
import ContactList from "@/layout/components/contact/ContactList/ContactList.vue"
import SvgIcon from "@/components/SvgIcon/SvgIcon.vue"
import ContactInfo from "@/layout/components/contact/ContactInfo/ContactInfo.vue"

const checkingUserInfo = ref<
    Partial<{
        id: string
        name: string
        avatar: string
    }>
>({})

const onChange = (id: string) => {
    console.log(id)
    checkingUserInfo.value = {
        id: "123",
        name: "KKuil",
        avatar: "https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJkQXoMolPPP0JVa8DF1kJ50nicQ1HJvYwXBoicBNVwlzlFNB23m0KCmd4AML7jE7icpwU7xCZJZ5pMA/132"
    }
}
</script>

<template>
    <ContactList class="hidden xl:block xl:flex-[25%]" @change="onChange" />
    <div
        class="flex-[1/2] flex-center sm:flex sm:flex-[76%] md:flex-[71%] xl:flex-[69%]"
    >
        <ContactInfo v-if="checkingUserInfo.id" :user="checkingUserInfo" />
        <el-empty v-else description="<-- 找个人聊聊天吧~">
            <template #image>
                <SvgIcon
                    icon-class="empty-contact"
                    class="w-full h-full flex-center"
                />
            </template>
        </el-empty>
    </div>
</template>

<style scoped lang="scss"></style>