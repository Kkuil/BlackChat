import { ref } from "vue"
import { defineStore } from "pinia"
import type { UserInfoType } from "@/stores/store"

export const useUserStore = defineStore("user", () => {
    const userInfo = ref<UserInfoType>({})
    return { userInfo }
})
