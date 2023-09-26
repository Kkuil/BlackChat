export declare namespace MessageResponseTypes {
    // 登录成功消息体类型
    type TLoginSuccess = {
        /**
         * 用户ID
         */
        uid: number
        /**
         * 用户头像信息
         */
        avatar: string
        /**
         * token
         */
        token: string
        /**
         * 用户名
         */
        name: string
    }
}
