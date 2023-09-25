import type { Emitter } from "mitt"
import mitt from "mitt"
import type { MsgReadUnReadCountType } from "@/services/types"

type Events = {
    focusMsgInput?: void
    onSelectPerson: { uid: number; ignoreCheck?: boolean }
    onAddReadCountTask: { msgId: number }
    onRemoveReadCountTask: { msgId: number }
    onGetReadCount: Map<number, MsgReadUnReadCountType>
}

const eventHub: Emitter<Events> = mitt<Events>()
export default eventHub
