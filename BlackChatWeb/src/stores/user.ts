import { ref } from "vue"
import { defineStore } from "pinia"
import type { Store } from "@/stores/store"
import { MessageResponseTypes } from "@/core/websocket/types/MessageResponseTypes"

export const useUserStore = defineStore("user", () => {
    const userInfo = ref<Store.UserInfoType>({})

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

    /**
     * 是否已经登录
     */
    const isLogin = () => {
        return !!userInfo.value.name
    }

    return { userInfo, loginSuccess, isLogin }
})
