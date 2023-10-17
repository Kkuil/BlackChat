import { Ref } from "vue"

const elMapCb = new WeakMap()

const ob: IntersectionObserver = new IntersectionObserver((entries) => {
    for (const entry of entries) {
        if (entry.isIntersecting) {
            const cb = elMapCb.get(entry.target)
            if (cb instanceof Function) {
                setTimeout(cb, 100)
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
