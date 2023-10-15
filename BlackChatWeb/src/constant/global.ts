// 源码地址
import { MessageTypeEnum } from "@/enums/MessageTypeEnum"

export const PROJECT_URL = "https://github.com/Kkuil/BlackChat"

// 全员群ID
export const HOT_GROUP_ID = 1

// 在回复消息中展示的内容
export const SHOW_IN_REPLY_MAP = {
    [MessageTypeEnum.FILE]: "[文件]",
    [MessageTypeEnum.IMAGE]: "[图片]",
    [MessageTypeEnum.SOUND]: "[语音]",
    [MessageTypeEnum.VIDEO]: "[视频]"
}

// 在会话列表中展示的内容
export const SHOW_IN_SESSION_MAP = {
    [MessageTypeEnum.FILE]: "[文件]",
    [MessageTypeEnum.IMAGE]: "[图片]",
    [MessageTypeEnum.SOUND]: "[语音]",
    [MessageTypeEnum.VIDEO]: "[视频]"
}
