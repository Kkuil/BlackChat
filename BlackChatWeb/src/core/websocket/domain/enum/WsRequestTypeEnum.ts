export enum WsRequestTypeEnum {
    /**
     * 请求登录二维码
     */
    LOGIN = 1001,

    /**
     * 心跳包
     */
    HEARTBEAT = 1002,

    /**
     * 登录认证
     */
    AUTHORIZE = 1003,

    /**
     * 退出登录
     */
    LOGOUT = 1004
}
