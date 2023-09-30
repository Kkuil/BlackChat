<script setup lang="ts">
import { computed, ref } from "vue"
import _ from "lodash"

const emits = defineEmits<{
    (e: "change", id: string): void
}>()

const props = defineProps({
    list: {
        type: Array
    },
    bgPrimaryColor: {
        default: "#272e3d"
    },
    bgSecondaryColor: {
        default: "#303442"
    },
    fontPrimaryColor: {
        default: "#ccc"
    },
    fontSecondaryColor: {
        default: "#f5f5f"
    }
})

const listRef = ref<HTMLDivElement>()

/**
 * 计算字母数组
 */
const computedLetters = computed(() => {
    const letters = []
    for (let i = 97; i <= 122; i++) {
        letters.push(String.fromCharCode(i))
    }
    return letters
})

/**
 * 排序
 */
const sortedList: object = computed(() => {
    const orderBy = _.orderBy(props.list, ["name"], ["asc"])
    const lower = orderBy.map((item) => ({
        ...item,
        name: item.name.toLowerCase()
    }))
    return _.groupBy(lower, (item) => item.name[0])
})

/**
 * 重定向索引
 * @param letter
 */
const redirectToIndex = (letter: string) => {
    let height = 0
    for (let i = 97; i <= 122; i++) {
        height += 25 + 10
        const word = String.fromCharCode(i).toLowerCase()
        if (word === letter) {
            break
        }
        console.log(word, sortedList, sortedList[word])
        const itemHeight = (sortedList.value[word]?.length ?? 0) * (10 + 40)
        height += itemHeight
    }
    console.log(height, listRef.value?.clientHeight, listRef.value?.scrollTop)
}

/**
 * 切换用户
 * @param e
 */
const check = (e: Event) => {
    const id = e.target?.dataset?.id
    emits("change", id)
}
</script>

<template>
    <div class="sorted-list">
        <div class="body">
            <div class="list" ref="listRef">
                <div
                    class="item"
                    v-for="letter in computedLetters"
                    :key="letter"
                >
                    <div v-show="sortedList[letter]" class="title-word">
                        {{ letter.toUpperCase() }}
                    </div>
                    <ul class="inner-list" @click="check">
                        <li
                            class="inner-item"
                            v-for="innerItem in sortedList[letter]"
                            :key="innerItem.name"
                            :data-id="innerItem.name"
                        >
                            <div class="assigned-el">
                                <el-avatar :size="30" :src="innerItem.avatar" />
                                <h3>{{ innerItem.name }}</h3>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
            <ul class="index">
                <li
                    class="dot"
                    v-for="letter in computedLetters"
                    :key="letter"
                    @click="redirectToIndex(letter)"
                >
                    {{ letter }}
                </li>
            </ul>
        </div>
    </div>
</template>

<style scoped>
.title-word {
    position: sticky;
    top: 0;
    width: 100%;
    margin-bottom: 10px;
    height: 25px;
    font-size: 13px;
    display: flex;
    align-items: center;
    padding: 0 10px;
    color: v-bind("fontPrimaryColor");
    border-radius: 5px;
    background-color: v-bind("bgSecondaryColor");
}

.sorted-list {
    height: 100%;
}

.body {
    display: flex;
    height: 100%;
    align-items: start;
}

.body .list {
    scroll-behavior: smooth;
    flex: 0.95;
    height: 510px;
    overflow-y: scroll;
}

.body .list::-webkit-scrollbar {
    width: 0;
}

.body .index {
    flex: 0.05;
    height: 500px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    margin-left: 5px;
}

.body .index .dot {
    cursor: pointer;
    display: flex;
    justify-content: center;
    align-items: center;
    width: 18px;
    height: 18px;
    font-size: 10px;
    background: v-bind("bgSecondaryColor");
    color: v-bind("fontPrimaryColor");
    border-radius: 50%;
}

.body .index .dot:hover {
    color: #0094ff;
}

.inner-list .inner-item {
    cursor: pointer;
    width: 100%;
    height: 40px;
    padding: 0 10px;
    display: flex;
    align-items: center;
    border-radius: 10px;
    margin-bottom: 10px;
}

.inner-list .inner-item .assigned-el {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    pointer-events: none;
}

.inner-list .inner-item:hover {
    background-color: v-bind("bgSecondaryColor");
}

.inner-list .inner-item h3 {
    margin-left: 7px;
    color: v-bind("fontPrimaryColor");
}
</style>