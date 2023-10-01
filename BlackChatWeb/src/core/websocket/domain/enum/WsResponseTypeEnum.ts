export enum WsResponseTypeEnum {
    /**
     * 连接成功
     */
    CONN_SUCCESS = 0,

    /**
     * 登录二维码返回
     */
    LOGIN_URL = 1,

    /**
     * 用户扫描成功等待授权
     */
    LOGIN_SUBSCRIBE_SUCCESS = 2,

    /**
     * 用户登录成功返回用户信息
     */
    LOGIN_SUCCESS = 3,

    /**
     * 新消息
     */
    MESSAGE = 4,

    /**
     * 上下线通知
     */
    ONLINE_OFFLINE_NOTIFY = 5,

    /**
     * 使前端的token失效，意味着前端需要重新登录
     */
    INVALIDATE_TOKEN = 6,

    /**
     * 拉黑用户
     */
    BLACK = 7,

    /**
     * 消息标记
     */
    MARK = 8,

    /**
     * 消息撤回
     */
    RECALL = 9,

    /**
     * 好友申请
     */
    APPLY = 10,

    /**
     * 成员变动
     */
    MEMBER_CHANGE = 11,

    /**
     * 更新上线列表
     */
    UPDATE_ONLINE_LIST = 12
}
