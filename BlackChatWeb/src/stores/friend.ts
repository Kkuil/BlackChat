import { defineStore } from "pinia"
import { listFriend } from "@/api/list"
import { useUserStore } from "@/stores/user"
import { updateByUidList } from "@/utils/userCache"

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
                this.friends = result.data.map((item) => {
                    if (userStore.userInfoCache[item.uid + ""]) {
                        item.avatar =
                            userStore.userInfoCache[item.uid + ""].avatar
                        item.name = userStore.userInfoCache[item.uid + ""].name
                    } else {
                        uidList.push(item.uid)
                    }
                    return item
                })
                if (uidList.length > 0) {
                    updateByUidList(uidList)
                    userStore.refreshCache()
                }
            }
        }
    }
})
