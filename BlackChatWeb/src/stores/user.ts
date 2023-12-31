import { computed, ref } from "vue"
import { defineStore } from "pinia"
import type { Store } from "@/stores/store"
import { MessageResponseTypes } from "@/core/websocket/types/MessageResponseTypes"
import { TEMP_USER_UID, TOKEN_KEY_IN_LOC } from "@/constant/auth"
import { USER_CACHE_LOC_KEY } from "@/constant/userKeys"
import { updateByUidList } from "@/utils/userCache"
import UserInfoCache = GlobalTypes.UserInfoCache

export const useUserStore = defineStore("user", () => {
    /**
     * 用户信息
     */
    const userInfo = ref<Store.UserInfoType>({})

    /**
     * 用户信息缓存
     */
    const userInfoCache = ref<Record<string, UserInfoCache>>(
        JSON.parse(localStorage.getItem(USER_CACHE_LOC_KEY) ?? "{}")
    )

    /**
     * 获取基本信息
     * @param uid 用户ID
     */
    const getBaseInfoInCache = (uid): UserInfoCache => {
        // TODO 更新缓存
        if (!userInfoCache.value[uid]) {
            let data = {}
            updateByUidList([uid]).then((users) => {
                data = users[0]
            })
            // 刷新缓存
            refreshCache()
            return data
        }
        return userInfoCache.value[uid]
    }

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
     * 刷新缓存
     */
    const refreshCache = () => {
        userInfoCache.value = JSON.parse(
            localStorage.getItem(USER_CACHE_LOC_KEY) ?? "{}"
        )
    }

    /**
     * 是否已经登录
     */
    const isLogin = computed(() => {
        return !!userInfo.value.uid
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

    /**
     * 是否是临时用户
     */
    const isTempUser = computed(() => {
        return userInfo.value.uid === TEMP_USER_UID || !userInfo.value.uid
    })

    return {
        userInfo,
        userInfoCache,
        loginSuccess,
        updateName,
        getBaseInfoInCache,
        refreshCache,
        isTempUser,
        isLogin,
        isAdmin
    }
})
