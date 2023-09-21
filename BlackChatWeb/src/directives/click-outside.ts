// click-outside.js
const ClickOutside = {
    // Vue 2
    bind(el: Element, binding, vnode) {
        el.clickOutsideEvent = function (event: Event) {
            if (!(el === event.target || el.contains(event.target))) {
                vnode.context[binding.expression](event)
            }
        }
        document.body.addEventListener("click", el.clickOutsideEvent)
    },
    unbind(el: Element) {
        document.body.removeEventListener("click", el.clickOutsideEvent)
    },
    // Vue 3
    beforeMount(el: Element, binding) {
        el.clickOutsideEvent = function (event) {
            if (!(el === event.target || el.contains(event.target))) {
                binding.value(event)
            }
        }
        document.body.addEventListener("click", el.clickOutsideEvent)
    },
    unmounted(el: Element) {
        document.body.removeEventListener("click", el.clickOutsideEvent)
    }
}

export default ClickOutside
