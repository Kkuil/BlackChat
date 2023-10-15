<script setup lang="ts">
import { onBeforeMount, ref } from "vue"
import ContactList from "@/layout/components/contact/ContactList/ContactList.vue"
import SvgIcon from "@/components/SvgIcon/SvgIcon.vue"
import ContactInfo from "@/layout/components/contact/ContactInfo/ContactInfo.vue"
import { useUserStore } from "@/stores/user"
import { useRouter } from "vue-router"
import { ElMessage } from "element-plus"
import { useFriendStore } from "@/stores/friend"
import FriendInfo = Store.FriendInfo

const userStore = useUserStore()
const friendStore = useFriendStore()

const $router = useRouter()

const checkingUserInfo = ref<Partial<FriendInfo>>({})

const onChange = (uid: string) => {
    checkingUserInfo.value = friendStore.getDetailByUid(Number(uid))
}

onBeforeMount(() => {
    if (!userStore.isLogin || userStore.isTempUser) {
        ElMessage.error("请先登录")
        $router.back()
    }
})
</script>

<template>
    <ContactList class="hidden xl:block xl:flex-[25%]" @change="onChange" />
    <div
        class="flex-[1/2] flex-center sm:flex sm:flex-[76%] md:flex-[71%] xl:flex-[69%]"
    >
        <ContactInfo v-if="checkingUserInfo.uid" :user="checkingUserInfo" />
        <el-empty
            v-else
            class="w-full h-full flex-center bg-primary"
            description="<-- 找个人聊聊天吧~"
        >
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