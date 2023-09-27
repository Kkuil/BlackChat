import type { App } from "vue"
import { createApp } from "vue"
import UserInfoDialog from "@/components/UserInfoDialog/UserInfoDialog.vue"

let app: App<HostElement>
let UserInfoCompContainer: HTMLDivElement

/**
 * 构建登录组件
 */
const createUserInfoComp = () => {
    app = createApp(UserInfoDialog)
}

/**
 * 弹出登录组件
 */
export const popUpUserInfoDialog = function () {
    createUserInfoComp()
    // 创建元素
    UserInfoCompContainer = document.createElement("div")
    document.body.appendChild(UserInfoCompContainer)
    app.mount(UserInfoCompContainer)
}

/**
 * 卸载登录组件
 */
export const popDownUserInfoDialog = function () {
    app.unmount()
    document.body.removeChild(UserInfoCompContainer)
}
