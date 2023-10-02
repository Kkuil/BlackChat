export declare namespace ChatItem {
    type ChatItemPropsType = Partial<{
        avatar: string
        name: string
        message: string
    }> & {
        extraInfo: ExtraInfo
    }
    type ExtraInfo = {
        sendTime: string
    }
}
