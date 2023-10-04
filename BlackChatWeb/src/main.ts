import { createApp } from "vue"
import { createPinia } from "pinia"
import "@/assets/styles/main.css"
import "element-plus/dist/index.css"
import "xgplayer/dist/index.min.css"
import "@imengyu/vue3-context-menu/lib/vue3-context-menu.css"
import "@/assets/styles/iconfont.js"

import "@/core/websocket/Websocket"
import "@/utils/eventListeners/baseListeners"
import App from "@/App.vue"
import router from "./router"
import SvgIcon from "@/components/SvgIcon/SvgIcon.vue"
import Observe from "@/directives/observe"
import clickOutside from "@/directives/click-outside"
import ContextMenu from "@imengyu/vue3-context-menu"

createApp(App)
const app = createApp(App)

app.use(ContextMenu)
app.use(createPinia())
app.use(router)
app.component("svg-icon", SvgIcon)
app.directive("observe", Observe)
app.directive("click-outside", clickOutside)

app.mount("#app")
