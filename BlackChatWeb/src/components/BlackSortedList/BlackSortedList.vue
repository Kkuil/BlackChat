<script setup lang="ts">
import { computed, ref } from "vue"
import pinyin from "pinyin"
import { Avatar } from "@element-plus/icons-vue"
import FriendInfo = Store.FriendInfo

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
const selectedId = ref<number>()

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
const sortedList = computed(() => {
    const pyMap: Record<string, (FriendInfo & { pinyin: string })[]> = {}
    props.list?.forEach((item: FriendInfo & { pinyin: string }) => {
        console.log(item.name)
        const pinyinA = pinyin(item.name, {
            style: pinyin.STYLE_NORMAL
        }).join("")
        item["pinyin"] = pinyinA
        const firstLetter = pinyinA[0].toUpperCase()
        const regex = /[0-9]/
        if (regex.test(firstLetter)) {
            if (pyMap["#"]) {
                pyMap["#"].push(item)
            } else {
                pyMap["#"] = [item]
            }
        } else {
            if (pyMap[firstLetter]) {
                pyMap[firstLetter].push(item)
            } else {
                pyMap[firstLetter] = [item]
            }
        }
    })

    for (const key of Object.keys(pyMap)) {
        pyMap[key] = pyMap[key].sort((a, b) => a.pinyin - b.pinyin)
    }
    console.log(pyMap)

    return pyMap
})

/**
 * 查看
 * @param e
 */
const check = (e: Event & { target: { dataset: { id: string } } }) => {
    const id = e.target?.dataset?.id
    if (id != null) {
        emits("change", id)
    }
    selectedId.value = Number(id)
}
</script>

<template>
    <div class="sorted-list">
        <div class="body">
            <div v-if="list && list.length" class="list" ref="listRef">
                <div
                    class="item"
                    v-for="letter in Object.keys(sortedList)"
                    :key="letter"
                >
                    <div class="title-word">
                        {{ letter }}
                    </div>
                    <ul class="inner-list" @click="check">
                        <li
                            class="inner-item"
                            :class="selectedId === item.uid ? 'selected' : ''"
                            v-for="item in sortedList[letter]"
                            :key="item.uid"
                            :data-id="item.uid"
                        >
                            <div class="assigned-el">
                                <el-avatar
                                    class="item-avatar"
                                    :size="30"
                                    :src="item.avatar"
                                >
                                    <el-icon :size="20">
                                        <Avatar />
                                    </el-icon>
                                </el-avatar>
                                <h3>{{ item.name }}</h3>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
            <el-empty v-else class="empty" description="暂无数据">
                <template #image>
                    <el-icon>
                        <i class="iconfont icon-empty"></i>
                    </el-icon>
                </template>
            </el-empty>
            <ul class="index">
                <li class="dot" v-for="letter in computedLetters" :key="letter">
                    {{ letter }}
                </li>
                <li class="dot">#</li>
            </ul>
        </div>
    </div>
</template>

<style scoped>
.title-word {
    z-index: 99;
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

.selected {
    background: v-bind("bgSecondaryColor");
}

.empty {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
}

.body {
    display: flex;
    height: 100%;
    align-items: start;
}

.icon-empty {
    color: #fff;
    font-size: 50px;
}

.body .list {
    scroll-behavior: smooth;
    flex: 0.95;
    height: 100%;
    overflow-y: scroll;
}

.body .list::-webkit-scrollbar {
    width: 0;
}

.body .index {
    flex: 0.05;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    margin-left: 5px;
}

.body .index .dot {
    cursor: pointer;
    display: flex;
    justify-content: center;
    align-items: center;
    width: 18px;
    height: 18px;
    margin: 1px 0;
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