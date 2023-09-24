export declare namespace ChatMessageItemTypes {
    type ChatItemStrategyType = {
        type: "text" | "system"
        configInfo: ChatMessageItemPropsType
    }
    /**
     * 普通消息
     */
    type ChatMessageItemPropsType = Partial<{
        avatar: string
        username: string
        message: string
        sendTime: string
        direction: "right" | "left"
        shape: "square" | "round"
        position: string
    }>

    /**
     * 系统消息
     */
    type ChatSystemItemPropsType = Partial<{
        message: string
    }>
}
