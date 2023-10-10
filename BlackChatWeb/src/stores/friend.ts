import { defineStore } from "pinia"
import { listFriend } from "@/api/list"
import { useUserStore } from "@/stores/user"
import { updateByUidList } from "@/utils/userCache"
import { delFriend } from "@/api/user"

const userStore = useUserStore()

export const useFriendStore = defineStore("friend", {
    state(): { friends: Store.FriendInfo[] } {
        return {
            friends: []
        }
    },
    actions: {
        async getFriends() {
            const result = await listFriend()
            if (result.data) {
                const uidList: number[] = []
                // 收集未缓存的数据
                result.data.forEach((item) => {
                    if (!userStore.userInfoCache[item.uid + ""]) {
                        uidList.push(item.uid)
                    }
                })
                // 获取缓存数据
                if (uidList.length > 0) {
                    await updateByUidList(uidList)
                    // 将缓存加载到内存中来
                    userStore.refreshCache()
                }
                // 缓存映射
                this.friends = result.data.map((item) => {
                    item.avatar =
                        userStore.userInfoCache[item.uid + ""]?.avatar ?? ""
                    item.name =
                        userStore.userInfoCache[item.uid + ""]?.name ?? ""
                    return item
                })
            }
        },
        getDetailByUid(uid: number) {
            return this.friends.filter((item) => item.uid === uid)[0]
        },
        async deleteFriend(uid: number) {
            console.log(123)
            const result = await delFriend(uid)
            if (result.data) {
                ElMessage.success("删除成功")
                this.friends = this.friends.filter((item) => item.uid !== uid)
            }
        }
    }
})
