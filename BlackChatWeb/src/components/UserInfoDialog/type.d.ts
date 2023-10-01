declare namespace UserInfoDialogCompTypes {
    type Badge = {
        /**
         * 徽章id
         */
        id: number

        /**
         * 徽章图标
         */
        img: string

        /**
         * 徽章描述
         */
        describe: string

        /**
         * 是否拥有 0否 1是
         */
        obtain: number

        /**
         * 是否佩戴  0否 1是
         */
        wearing: number
    }
}
