import moment from "moment"

const THREE_SECONDS = 3000
const TEN_SECONDS = 10000
const ONE_MINUTE = 60000
const TWO_MINUTES = 120000
const FIVE_MINUTES = 300000
const ONE_HOUR = 3600000
const TWO_HOURS = 7200000
const YESTERDAY = 86400000
const THE_DAY_BEFORE_YESTERDAY = 172800000

export function formatTimestamp(date: Date): string {
    if (!date) {
        return ""
    }
    // 1. 获取当前时间戳
    const cur_timestamp = Date.now()
    const date_timestamp = date.getTime()

    console.log(cur_timestamp, date)

    // 2. 计算时间差
    const diff = cur_timestamp - date_timestamp
    if (diff < THREE_SECONDS) {
        return "刚刚"
    } else if (diff < TEN_SECONDS) {
        return "几秒前"
    } else if (diff < ONE_MINUTE) {
        return Math.floor(diff / 1000) + "秒前"
    } else if (diff < TWO_MINUTES) {
        return "1分钟前"
    } else if (diff < FIVE_MINUTES) {
        return Math.floor(diff / 60000) + "分钟前"
    } else if (diff < ONE_HOUR) {
        return "1小时前"
    } else if (diff < TWO_HOURS) {
        return "2小时前"
    } else if (diff < YESTERDAY) {
        return Math.floor(diff / 3600000) + "小时前"
    } else if (diff < THE_DAY_BEFORE_YESTERDAY) {
        return (
            "昨天 " +
            moment(date_timestamp - THE_DAY_BEFORE_YESTERDAY).format("HH:mm")
        )
    } else {
        return moment(date).format("YYYY-MM-DD")
    }
}
