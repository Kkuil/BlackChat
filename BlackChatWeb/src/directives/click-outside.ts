export default {
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
