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
     * 更新上线列表
     */
    UPDATE_ONLINE_LIST = 1004
}
