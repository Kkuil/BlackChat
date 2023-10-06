import { Ref } from "vue"
import _ from "lodash"

const elMapCb = new WeakMap()

const ob: IntersectionObserver = new IntersectionObserver((entries) => {
    for (const entry of entries) {
        if (entry.isIntersecting) {
            const cb = elMapCb.get(entry.target)
            if (cb instanceof Function) {
                _.throttle(cb, 500)()
            }
        }
    }
})

export default {
    mounted(el: Element, binding: Ref) {
        if (!(binding?.value instanceof Function)) {
            return
        }
        ob.observe(el)
        elMapCb.set(el, binding.value)
    },
    unmounted(el: Element) {
        ob.unobserve(el)
    }
}
