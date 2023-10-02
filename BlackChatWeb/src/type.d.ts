declare namespace GlobalTypes {
    // 统一返回结果格式
    type ApiResult<T> = {
        code: number
        message: string
        data: T
    }
    // 游标格式
    type CursorPageReq = {
        pageSize: number
        cursor: number | null
        isLast: boolean
    }
    // 游标格式
    type CursorPageResp<T> = {
        pageSize: number
        cursor: number | null
        isLast: boolean
        list: T
        extraInfo: any
    }
    // 翻页格式
    type LimitPage<T> = {
        pageSize: number
        current: number | null
        data: T
    }
    // 用户信息格式
    type UserInfo = Partial<{
        uid: number
        name: string
        avatar: string
        activeStatus: number
    }>
    // 基本消息格式
    type Message = {
        id: number
        content: string
        sendTime: number
        sender: UserInfo
    }
}
