import { ref } from "vue"
import { defineStore } from "pinia"
import type { UserInfoType } from "@/stores/store"
import { MessageResponseTypes } from "@/core/websocket/types/MessageResponseTypes"

export const useUserStore = defineStore("user", () => {
    const userInfo = ref<UserInfoType>({})

    /**
     * 登录成功保存信息
     * @param info
     */
    const loginSuccess = (info: MessageResponseTypes.TLoginSuccess) => {
        userInfo.value = {
            ...userInfo.value,
            ...info
        }
    }

    return { userInfo, loginSuccess }
})
