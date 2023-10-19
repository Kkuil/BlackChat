import request from "@/utils/request"
import {
    TOKEN_KEY_IN_HEADER,
    TOKEN_KEY_IN_LOC,
    TOKEN_PREFIX
} from "@/constant/auth"
import ApiResult = GlobalTypes.ApiResult

/**
 * 退出群聊
 * @param groupId 房间ID
 */
export const exitGroup = (groupId: number): Promise<ApiResult<boolean>> => {
    return request({
        url: "/group/exit",
        method: "DELETE",
        params: {
            groupId
        },
        headers: {
            [TOKEN_KEY_IN_HEADER]:
                TOKEN_PREFIX + localStorage.getItem(TOKEN_KEY_IN_LOC) || ""
        }
    })
}

/**
 * 添加管理
 * @param data 请求数据
 */
export const addAdmin = (data: {
    groupId: number
    uidList: number[]
}): Promise<ApiResult<boolean>> => {
    return request({
        url: "/group/admin/add",
        method: "PUT",
        data,
        headers: {
            [TOKEN_KEY_IN_HEADER]:
                TOKEN_PREFIX + localStorage.getItem(TOKEN_KEY_IN_LOC) || ""
        }
    })
}

/**
 * 取消管理
 * @param data 请求数据
 */
export const delAdmin = (data: {
    groupId: number
    uidList: number[]
}): Promise<ApiResult<boolean>> => {
    return request({
        url: "/group/admin/update",
        method: "PUT",
        data,
        headers: {
            [TOKEN_KEY_IN_HEADER]:
                TOKEN_PREFIX + localStorage.getItem(TOKEN_KEY_IN_LOC) || ""
        }
    })
}

/**
 * 更新群信息
 * @param data 请求数据
 */
export const updateGroupInfo = (data: {
    groupId: number
    groupName?: string
    groupAvatar?: string
}): Promise<ApiResult<boolean>> => {
    return request({
        url: "/group/info/update",
        method: "PUT",
        data,
        headers: {
            [TOKEN_KEY_IN_HEADER]:
                TOKEN_PREFIX + localStorage.getItem(TOKEN_KEY_IN_LOC) || ""
        }
    })
}
