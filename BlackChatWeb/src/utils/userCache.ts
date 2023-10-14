import UserInfo = GlobalTypes.UserInfo
import UserInfoCache = GlobalTypes.UserInfoCache
import UserBaseInfo = GlobalTypes.UserBaseInfo
import ApiResult = GlobalTypes.ApiResult
import {
    USER_CACHE_EXPIRE_TIME,
    USER_CACHE_LOC_KEY,
    USER_MAX_CACHE_COUNT_LOC_KEY
} from "@/constant/userKeys"
import { listBatchCache } from "@/api/user";

/**
 * 更新本地缓存
 * @param list 用户列表
 */
export function updateUserInfoCache(list: UserInfo[]) {
    const localUserInfo: Record<string, UserInfoCache> = JSON.parse(
        localStorage.getItem(USER_CACHE_LOC_KEY) ?? "{}"
    )
    // 计算需要淘汰的缓存key数量
    const keys = Object.keys(localUserInfo)
    const count = keys.length + list.length - USER_MAX_CACHE_COUNT_LOC_KEY
    if (count > 0) {
        const sortedKeys = keys.sort(
            (key1, key2) =>
                localUserInfo[key2].expireTime - localUserInfo[key1].expireTime
        )
        const needRemovedKeys = sortedKeys.slice(0, count)
        needRemovedKeys.forEach((key) => delete localUserInfo[key])
    }

    // 判断缓存是否达到上限
    list.forEach((userInfo) => {
        localUserInfo[userInfo.id] = {
            uid: userInfo.id,
            name: userInfo.name,
            avatar: userInfo.avatar,
            expireTime: Date.now() + USER_CACHE_EXPIRE_TIME
        }
    })
    localStorage.setItem(USER_CACHE_LOC_KEY, JSON.stringify(localUserInfo))
}

/**
 * 更新本地缓存
 * @param list 用户列表
 */
export async function updateByUidList(
    list: number[]
): Promise<ApiResult<UserBaseInfo[]>> {
    const result = await listBatchCache(list)
    updateUserInfoCache(result.data)
    return result.data
}
