import axios from "axios"

const BASE_URL: string = import.meta.env.VITE_REQUEST_BASE_URL
const BASE_TIMEOUT: number = import.meta.env.VITE_REQUEST_TIMEOUT
const BASE_PREFIX: string = import.meta.env.VITE_REQUEST_BASE_PREFIX

console.log(BASE_URL, BASE_TIMEOUT, BASE_PREFIX)

const request = axios.create({
    baseURL: BASE_URL + BASE_PREFIX,
    timeout: BASE_TIMEOUT
})

// 请求拦截器
request.interceptors.request.use(
    (config) => {
        return config
    },
    (error) => {
        console.log("error: ", error)
    }
)

// 响应拦截器
request.interceptors.response.use(
    (response) => {
        return response.data
    },
    (error) => {
        console.log("error: ", error)
    }
)

export default request
