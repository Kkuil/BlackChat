declare namespace Store {
    type UserInfoType = {
        /** 用户唯一标识 */
        uid: number
        /** 用户头像 */
        avatar: string
        /** 用户名 */
        name: string
        /** 剩余改名次数 */
        modifyNameChance: number
        /** 性别 1为男性，2为女性 */
        sex: SexEnum
        /** 徽章，本地字段，有值用本地，无值用远端 */
        badge?: string
        /** 权限 */
        power?: number
    }
    type FriendInfo = {
        uid: number
        roomId: number
        name: string
        avatar: string
        place: string
    }
}
