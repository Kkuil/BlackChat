/// <reference types="vite/client" />

interface ImportMetaEnv {
    // 请求基础路径
    readonly VITE_REQUEST_BASE_URL: string
    // 请求基础前缀
    readonly VITE_REQUEST_BASE_PREFIX: string
    // 请求基础超时时间
    readonly VITE_REQUEST_TIMEOUT: string
    // websocket请求路径
    readonly VITE_WS_URL: string
}

interface ImportMeta {
    readonly env: ImportMetaEnv
}
