import { ref } from "vue"
import { defineStore } from "pinia"
import type { Store } from "@/stores/store"
import { MessageResponseTypes } from "@/core/websocket/types/MessageResponseTypes"
import { TOKEN_KEY_IN_LOC } from "@/constant/auth"

export const useUserStore = defineStore("user", () => {
    const userInfo = ref<Store.UserInfoType>({})

    /**
     * 登录成功保存信息
     * @param info
     */
    const loginSuccess = (info: MessageResponseTypes.TLoginSuccess) => {
        userInfo.value = {
            ...userInfo.value,
            uid: info.uid,
            avatar: info.avatar,
            name: info.name
        }
        // 保存token
        localStorage.setItem(TOKEN_KEY_IN_LOC, info.token)
    }

    /**
     * 是否已经登录
     */
    const isLogin = () => {
        return !!userInfo.value.name
    }

    return { userInfo, loginSuccess, isLogin }
})
