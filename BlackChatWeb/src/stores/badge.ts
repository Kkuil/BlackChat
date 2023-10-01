import { defineStore } from "pinia"
import { ref } from "vue"
import { listBadge } from "@/api/chat/list"
import { BADGE_INFO_LOC_PREFIX_KEY } from "@/constant/userKeys"

export const useBadgeStore = defineStore(
    "badge",
    () => {
        const list = ref<UserInfoDialogCompTypes.Badge[]>([])

        /**
         * 更新列表
         * @param pageInfo 分页信息
         */
        const updateBadges = async (pageInfo: GlobalTypes.LimitPage<any>) => {
            // 1. 从本地缓存中获取数据
            const cache = localStorage.getItem(
                BADGE_INFO_LOC_PREFIX_KEY + pageInfo.current
            )
            if (cache) {
                return (list.value = JSON.parse(cache))
            }

            // 2. 从服务器获取数据
            const result = await listBadge(pageInfo)
            if (!result.data) {
                return false
            }
            list.value = result.data

            // 3. 缓存数据
            localStorage.setItem(
                BADGE_INFO_LOC_PREFIX_KEY + pageInfo.current,
                JSON.stringify(result.data)
            )
            return true
        }

        return { list, updateBadges }
    },
    {}
)
