declare namespace GlobalTypes {
    type ApiResult<T> = {
        code: number
        message: string
        data: T
    }
}
