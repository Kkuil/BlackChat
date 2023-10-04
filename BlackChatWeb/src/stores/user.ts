import { computed, ref } from "vue"
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
    const isLogin = computed(() => {
        return !!userInfo.value.name
    })

    /**
     * 是否是管理员
     */
    const isAdmin = computed(() => {
        return false
    })

    /**
     * 更新用户名
     * @param username 用户名
     */
    const updateName = (username: string) => {
        userInfo.value.name = username
    }

    return { userInfo, loginSuccess, updateName, isLogin, isAdmin }
})
