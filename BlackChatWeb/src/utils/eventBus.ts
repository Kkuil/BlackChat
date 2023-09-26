import type { Emitter } from "mitt"
import mitt from "mitt"

type Events = {
    refresh_qr_code: void
    get_qr_code: { url: string }
}

const eventHub: Emitter<Events> = mitt<Events>()
export default eventHub
