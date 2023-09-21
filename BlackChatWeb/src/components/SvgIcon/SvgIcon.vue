<script setup lang="ts">
import { computed } from "vue"

const props = defineProps<{
    iconClass: string
}>()

const iconName = computed(() => `#icon-${props.iconClass}`)
</script>

<template>
    <svg class="svg-icon flex-center" aria-hidden="true">
        <use :xlink:href="iconName"></use>
    </svg>
</template>

<style scoped lang="scss">
.svg-icon {
    width: 1.7em;
    height: 1.7em;
    overflow: hidden;
}
</style>