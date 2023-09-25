import { createApp } from "vue"
import { createPinia } from "pinia"
import "@/assets/styles/main.css"
import "@/assets/styles/iconfont.js"

import "@/core/websocket/Websocket"

import App from "@/App.vue"
import router from "./router"
import SvgIcon from "@/components/SvgIcon/SvgIcon.vue"

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.component("svg-icon", SvgIcon)

app.mount("#app")
