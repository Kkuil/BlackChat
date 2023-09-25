import { LoginDialogCompTypes } from "@/components/LoginDialog/type"
import type { App } from "vue"
import { createApp } from "vue"
import LoginDialog from "@/components/LoginDialog/LoginDialog.vue"

let app: App<HostElement>
let loginCompContainer: HTMLDivElement

/**
 * 构建登录组件
 * @param props
 */
const createLoginComp = (props: LoginDialogCompTypes.LoginDialogCompProps) => {
    app = createApp(LoginDialog, props)
}

/**
 * 弹出登录组件
 * @param props
 */
export const popUpLoginDialog = function (
    props: LoginDialogCompTypes.LoginDialogCompProps
) {
    createLoginComp(props)
    // 创建元素
    loginCompContainer = document.createElement("div")
    document.body.appendChild(loginCompContainer)
    app.mount(loginCompContainer)
}

/**
 * 卸载登录组件
 */
export const popDownLoginDialog = function () {
    app.unmount()
    document.body.removeChild(loginCompContainer)
}
