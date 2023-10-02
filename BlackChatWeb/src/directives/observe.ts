// observe.ts
let observer: IntersectionObserver
const Observe = {
    // Vue 3
    beforeMount(el: Element, binding) {
        console.log(binding)
        observer = new IntersectionObserver((entries) => {
            entries.forEach((entry) => {
                if (entry.isIntersecting) {
                    binding.value()
                    observer.unobserve(el)
                    setTimeout(() => {
                        observer.observe(el)
                    }, 500)
                }
            })
        })
    },
    mounted(el: Element, binding) {
        observer.observe(el)
    },
    unmounted(el: Element) {
        observer.unobserve(el)
    }
}

export default Observe
